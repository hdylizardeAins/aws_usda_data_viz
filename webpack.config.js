var path = require('path')
var webpack = require('webpack')
const HtmlWebpackPlugin = require('html-webpack-plugin')
var HardSourceWebpackPlugin = require('hard-source-webpack-plugin');

//Prevent Hard Source plugin from causing Maven errors; see https://github.com/mzgoddard/hard-source-webpack-plugin/issues/247
function SilentHardSource() {}
SilentHardSource.prototype.apply = function (compiler) {
    compiler.plugin('hard-source-log', function (message) {
        if (message.level !== 'error' && message.level !== 'warn') {
            console[message.level].call(
                console,
                'hard-source:' + message.from, message.message
            );
        }
    });
}

module.exports = {
    entry: {
        app: './src/main/js/main.js',
        vendor: ['vue', 'vuex', 'axios', 'element-ui']
    },
    output: {
        path: path.resolve(__dirname, './src/main/webapp/js/'),
        publicPath: 'js/',
        filename: '[name].js',
        sourceMapFilename: '[name].js.map'
    },
    module: {
        rules: [{
                test: /\.css$/,
                use: [
                    'vue-style-loader',
                    'css-loader'
                ],
            }, {
                test: /\.vue$/,
                loader: 'vue-loader'
            },
            {
                test: /\.js$/,
                loader: 'babel-loader',
                exclude: /node_modules/
            },
            {
                test: /\.(png|jpg|gif|svg|ttf|woff|eot|woff2)$/,
                loader: 'file-loader',
                options: {
                    name: '[name].[ext]'
                }
            }
        ]
    },
    resolve: {
        alias: {
            'vue$': 'vue/dist/vue.esm.js'
        },
        extensions: ['*', '.js', '.vue', '.json']
    },
    devServer: {
        historyApiFallback: true,
        noInfo: true,
        overlay: true,
        contentBase: path.resolve(__dirname, './src/main/webapp/'),
        port: 8081,
        proxy: [{
            context:['/analytics', '/datasets'],
            target: 'http://localhost:8080/',
            secure: false
        }],
        historyApiFallback: {
            index: 'dev.html',
        },
    },
    plugins: [
        new webpack.NormalModuleReplacementPlugin(/element-ui[\/\\]lib[\/\\]locale[\/\\]lang[\/\\]zh-CN/, 'element-ui/lib/locale/lang/en'),
        new webpack.optimize.CommonsChunkPlugin({
            names: ['vendor']
        }),
        //Decrease build time with hard source plugin (better caching of built files)
        new HardSourceWebpackPlugin(),
        new SilentHardSource()
    ]

}

if (process.env.NODE_ENV === 'production') {
    // http://vue-loader.vuejs.org/en/workflow/production.html
    module.exports.plugins = (module.exports.plugins || []).concat([
        new webpack.DefinePlugin({
            'process.env': {
                NODE_ENV: '"production"'
            }
        }),
        new webpack.optimize.UglifyJsPlugin({
            sourceMap: false,
            compress: {
                warnings: false
            }
        }),
        new webpack.LoaderOptionsPlugin({
            minimize: true
        })
    ])
} else {
    module.exports.devtool = '#source-map'
    module.exports.plugins = (module.exports.plugins || []).concat([
        new webpack.DefinePlugin({
            'process.env': {
                NODE_ENV: '"development"'
            }
        }),
        new webpack.LoaderOptionsPlugin({
            minimize: false
        }),
        //Auto-generate static html file for use by npm run dev, since we have JSP file by default which isn't supported
        new HtmlWebpackPlugin({
            title: 'USDA Data Vis Dev',
            filename: '../dev.html'
        })
    ])
}