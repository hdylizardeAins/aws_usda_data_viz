<template>
    <el-form>
        <h4>3a. Choose Variables</h4>
        <el-row>
            <el-col class="bordered-panel" id="x-axis" :span="11">
                X Axis
                <el-checkbox-group v-model="form.type">
                    <el-checkbox label="x variable 1" name="type"></el-checkbox>
                    <el-checkbox label="x variable 2" name="type"></el-checkbox>
                    <el-checkbox label="x variable 3" name="type"></el-checkbox>
                </el-checkbox-group>
            </el-col>
            <el-col class="bordered-panel" id="y-axis" :span="11">
                Y Axis
                <el-checkbox-group v-model="form.type">
                    <el-checkbox label="y variable 1" name="type"></el-checkbox>
                    <el-checkbox label="y variable 2" name="type"></el-checkbox>
                    <el-checkbox label="y variable 3" name="type"></el-checkbox>
                </el-checkbox-group>
            </el-col>
        </el-row>
    </el-form>
</template>

<script>
export default {
    data() {
        return {
            form: {} // Extract this to v-model
        }
    }
}
</script>
