<template>
    <el-form :model="analytic" label-position="top">
        <h4>3a. Choose Variables</h4>
        <el-row>
            <el-col class="bordered-panel" id="x-axis" :span="11">
                <el-form-item prop="yVars" label="X Axis">
                    <el-checkbox-group v-model="analytic.xVars">
                        <el-checkbox v-for="column in analytic.columns" :key="column" :label="column" name="name"></el-checkbox>
                    </el-checkbox-group>
                </el-form-item>
            </el-col>
            <el-col class="bordered-panel" id="y-axis" :span="11">
                <el-form-item prop="yVars" label="Y Axis">
                    <el-checkbox-group v-model="analytic.yVars">
                        <el-checkbox v-for="column in analytic.columns" :key="column" :label="column" name="column"></el-checkbox>
                    </el-checkbox-group>
                </el-form-item>
            </el-col>
        </el-row>
        <el-row>
            <el-button type="primary" @click="submitForm">Run Analytic</el-button>
        </el-row>
    </el-form>
</template>

<script>
import deepCopy from './DeepCopy.js';
import EventBus from './EventBus.vue';

export default {
    props: [
        "analytic"
    ],
    data() {
        return {
            formData: {
                xVars: [],
                yVars: []
            }
        }
    },
    methods: {
        submitForm() {
            EventBus.$emit("waitForExecution");
            let analyticToSubmit = deepCopy(this.analytic);
            analyticToSubmit.xVars = deepCopy(this.formData.xVars);
            analyticToSubmit.yVars = deepCopy(this.formData.yVars);
            let dataset = deepCopy(this.$store.getters.selectedDataset);
            let payload = {
                analyticName: analyticToSubmit.name,
                datasetFilePath: dataset.filePath,
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
                        this.$store.commit('updateExecutions', {
                            datasetName: dataset.name,
                            analyticName: analyticToSubmit.name,
                            imagePath: response.data.outputFile //TODO: this is just the filename - update with relative path on the server
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