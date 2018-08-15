<template>
    <el-form :model="executionFormData" label-position="top">
        <h4>3a. Choose Variables</h4>
        <el-row v-if="executionFormData.analytic.requiresXY">
            <el-col class="bordered-panel" id="x-axis" :span="11">
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
            <el-col class="bordered-panel" id="y-axis" :span="11">
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
        <el-row class="bordered-panel" v-else>
            <el-form-item prop="columns" label="Columns">
                    <el-checkbox-group v-model="execution.columns">
                        <el-checkbox v-for="column in executionFormData.dataset.columns" :key="column" :label="column" name="column"></el-checkbox>
                    </el-checkbox-group>
                </el-form-item>
        </el-row>
        <el-row>
            <el-button type="primary" @click="submitForm" :disabled="runButtonDisabled">Run Analytic</el-button>
        </el-row>
    </el-form>
</template>

<script>
import deepCopy from './DeepCopy.js';
import EventBus from './EventBus.vue';
import Constants from './Constants.js';

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