<template>
    <el-form v-loading="loading" :model="executionFormData" label-position="top">
        <h4>3a. Choose Variables</h4>
        <el-row v-if="executionFormData.analytic.requiresXY">
            <el-col class="bordered-panel" id="x-axis" :span="11">
                <el-form-item prop="yVars" label="X Axis">
                    <el-checkbox-group v-model="executionFormData.xVars" :max="1">
                        <el-checkbox v-for="column in executionFormData.dataset.columns" :key="column" :label="column" name="name"></el-checkbox>
                    </el-checkbox-group>
                </el-form-item>
            </el-col>
            <el-col class="bordered-panel" id="y-axis" :span="11">
                <el-form-item prop="yVars" label="Y Axis">
                    <el-checkbox-group v-model="execution.yVars" :max="1">
                        <el-checkbox v-for="column in executionFormData.dataset.columns" :key="column" :label="column" name="column"></el-checkbox>
                    </el-checkbox-group>
                </el-form-item>
            </el-col>
        </el-row>
        <el-row class="bordered-panel" v-else>
            <el-form-item prop="columns" label="Columns">
                    <el-checkbox-group v-model="execution.columns">
                        <el-checkbox v-for="column in executionFormData.dataset.columns" :key="column" :label="column" name="column"></el-checkbox>
                    </el-checkbox-group>
                </el-form-item>
        </el-row>
        <el-row>
            <el-button type="primary" @click="submitForm">Run Analytic</el-button>
        </el-row>
    </el-form>
</template>

<script>
import deepCopy from './DeepCopy.js';
import EventBus from './EventBus.vue';

const apachePort = "8080";

export default {
    props: [
        "execution"
    ],
    data() {
        return {
            executionFormData: this.execution
        }
    },
    computed:{
        loading: function(){
            return false; //TODO: replace this with a condition that returns true until the execution has the columns from the back end if applicable to the analytic
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
            let executionToSubmit = deepCopy(this.executionFormData);
            let params = {
                inputFile: executionToSubmit.dataset.filePath,
            };
            if(executionToSubmit.analytic.requiresXY) {
                params.x = executionToSubmit.xVars[0];
                params.y = executionToSubmit.yVars[0];
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
                        EventBus.$emit("executionFinished");
                    }.bind(this),
                    success: function(response){
                        //Update the executions store
                        let path = "";
                        if (response.data.outputFile){
                            path = window.location.protocol + "//" + window.location.hostname + ":" + apachePort + "/" + response.data.outputFile;
                        }
                        this.$store.commit('updateExecution', {
                            dataset: self.execution.dataset,
                            analytic: self.execution.analytic,
                            imagePath: path
                        });
                        EventBus.$emit("executionFinished");
                    }.bind(this)
                }
            }
            this.$store.dispatch("requestAnalyticExec", payload);
        }
    }
}
</script>