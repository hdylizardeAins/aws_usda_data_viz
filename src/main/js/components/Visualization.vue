<template>
    <div id="visualization-panel">
        <el-container>
            <el-row>3. View and Tailor Visualizations</el-row>
            <el-row>
                <el-col>
                    <el-tabs v-model="currentTab" type="card">
                        <el-tab-pane
                            v-for="item in editableTabs2"
                            :key="item.name"
                            :label="item.title"
                            :name="item.name"
                        >
                        {{item.content}}
                        </el-tab-pane>
                    </el-tabs>
                </el-col>
            </el-row>
        </el-container>
    </div>
</template>

<script>
export default {
    data() {
        return {
            currentTab: 'No Analytic'
        }
    },
    computed: {
        selectedAnalytics: function(){
            return this.$store.getters.selectedAnalytics;
        },
        editableTabs2: function(){
            
            if (this.selectedAnalytics.length == 0){
                return [
                        {
                            title: 'No Analytic',
                            name: 'No Analytic',
                            content: 'Please select 1 or more Analytics and click Next'
                        }
                    ]
            }
            else{
                let tabs = [];
                for (let a of this.selectedAnalytics){
                    let contentStr = "Content for " + a.name;
                    tabs.push({
                        title: a.name,
                        name: a.name,
                        content: contentStr
                    })
                }
                return tabs;
            }
        }
    },
    methods: {
        
    }
};
</script>
<style>
#visualization-panel {
    border: solid 1px gray;
    padding: 4px;    
    margin: 2px;
}
</style>
