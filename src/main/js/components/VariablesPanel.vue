<template>
    <el-form :model="executionFormData" label-position="top">
        <div><span><strong>3a. Choose Columns</strong></span></div>
        <el-row class="execution-vars-form" v-if="executionFormData.analytic.requiresXY">
            <el-col class="bordered-panel-top bordered-panel" id="x-axis" :span="11">
                <el-form-item prop="xVars" label="X Axis">
                    <el-select v-model="execution.xVars" placeholder="Select X">
                        <el-option
                            v-for="item in filteredXAxis"
                            :key="item"
                            :label="item"
                            :value="item" />
                    </el-select>
                </el-form-item>
            </el-col>
            <el-col class="bordered-panel-top bordered-panel" id="y-axis" :span="11">
                <el-form-item prop="yVars" label="Y Axis">
                    <el-select v-model="execution.yVars" placeholder="Select Y">
                        <el-option
                            v-for="item in filteredYAxis"
                            :key="item"
                            :label="item"
                            :value="item" />
                    </el-select>
                </el-form-item>
            </el-col>
        </el-row>
        <el-row class="execution-vars-form bordered-panel bordered-panel-top" v-else>
            <el-form-item prop="columns" label="Columns">
                    <el-checkbox-group v-model="execution.columns">
                        <el-checkbox v-for="column in executionFormData.dataset.columns" :key="column" :label="column" name="column"></el-checkbox>
                    </el-checkbox-group>
                </el-form-item>
        </el-row>
        <el-row>
            <el-col>
                <el-button id="runAnalyticBtn" type="primary" class="greenBtn" @click="submitForm" :disabled="runButtonDisabled">Run Analytic</el-button>
                <el-button id="postImgBtn" type="primary" @click="handlePostClick" :disabled="postButtonDisabled">Post</el-button>
            </el-col>
            <post-form :show-dialog="showPostForm" :post-data="postData" @close-post-dialog="showPostForm = false" />
        </el-row>
    </el-form>
</template>

<script>
import deepCopy from './DeepCopy.js';
import EventBus from './EventBus.vue';
import Constants from './Constants.js';
import PostForm from './PostForm.vue';

export default {
    components:{
        PostForm
    },
    props: [
        "execution"
    ],
    data() {
        return {
            executionFormData: this.execution,
            showPostForm: false,
            postData: {},
            postDisabled: false
        }
    },
    computed:{
        filteredXAxis: function(){
            if (this.execution.dataset.columns){
                return this.execution.dataset.columns.filter(c => !(this.executionFormData.yVars && this.executionFormData.yVars.includes(c)));
            }
            return []
        },
        filteredYAxis: function(){
            if (this.execution.dataset.columns){
                return this.execution.dataset.columns.filter(c => !(this.executionFormData.xVars && this.executionFormData.xVars.includes(c)));
            }
            return []
        },
        runButtonDisabled: function(){
            if (this.executionFormData.analytic.requiresXY){
                return !(this.executionFormData.xVars && this.executionFormData.xVars.length > 0 && this.executionFormData.yVars && this.executionFormData.yVars.length > 0);
            }
            return this.executionFormData.columns.length == 0;
        },
        postButtonDisabled: function(){
            return this.postDisabled || typeof this.execution.imagePath !== 'string' || this.execution.imagePath.length  < 1;
        }
    },
    watch: {
        execution: function(newExecution) {
            this.executionFormData = Object.assign({}, this.executionFormData, deepCopy(newExecution));
        }
    },
    methods: {
        submitForm() {
            EventBus.$emit("waitForExecution");
            this.postDisabled = true;
            let executionToSubmit = deepCopy(this.executionFormData);
            let params = {
                inputFile: executionToSubmit.dataset.filePath,
            };
            if(executionToSubmit.analytic.requiresXY) {
                params.x = executionToSubmit.xVars;
                params.y = executionToSubmit.yVars;
            } else {
                params.columns = executionToSubmit.columns.join();
            }

            let self = this;
            let payload = {
                analyticName: executionToSubmit.analytic.name,
                params: params,
                callback: {
                    failure: function(err) {
                        this.$notify.error({
                            message: "Unable to generate analytic: " + err.message,
                            customClass: "error",
                            duration: 5000,
                            title: "Error"
                        });
                        this.postDisabled = false;
                        EventBus.$emit("executionFinished");
                    }.bind(this),
                    success: function(response){
                        //Update the executions store
                        let path = "";
                        if (response.data.outputFile){
                            path = Constants.getApacheUrlPrefix() + response.data.outputFile;
                        }
                        this.$store.commit('updateExecution', {
                            dataset: self.execution.dataset,
                            analytic: self.execution.analytic,
                            imagePath: path,
                            graphText: response.data.text
                        });
                        this.postDisabled = false;
                        EventBus.$emit("executionFinished");
                    }.bind(this)
                }
            }
            this.$store.dispatch("requestAnalyticExec", payload);
        },
        handlePostClick(){
            this.postData = {
                imageName: this.execution.imagePath,
                graphData: this.execution.graphText
            }
            this.showPostForm = true;
        }
    }
}
</script>
<style>
#runAnalyticBtn {
    float:right;
    position:relative;
}
#postImgBtn {
    float:left;
    position:relative;
}

.bordered-panel-top {
    border-top: solid 1px gray;
    padding-top: 4px;
}
</style>
<style scope>
.execution-vars-form  .el-form-item__label {
    width: 100%;
    margin: auto;
    font-weight: bold;
    text-align: center;
}

.execution-vars-form .el-select {
    width: 80%;
    margin: auto;
    display: block;
}
</style>