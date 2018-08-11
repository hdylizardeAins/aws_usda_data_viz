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
            this.formData.name = this.analytic.name;
            let payload = {
                analytic: this.formData,
                handler: {
                      failure: function(err) {
                        this.$notify.error({
                          message: "Unable to generate analytic: " + err.message,
                          customClass: "error",
                          duration: 5000,
                          title: "Error"
                        });
                      }.bind(this)
                    }
            }
            this.$store.dispatch("requestAnalyticExec", payload);
        }
    }
}
</script>