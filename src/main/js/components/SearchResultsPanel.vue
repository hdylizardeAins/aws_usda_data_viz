<template>
<el-container>
    <el-row type="flex" v-loading="isLoading">
        <el-col class="search-flex" :span="24">
    <el-table :data="searchResults">
        <el-table-column prop="name" label="name" />
        <el-table-column prop="description" label="description" />
        <el-table-column>
            <template slot-scope="scope">
            <el-button @click="fetch(scope.row)">Import</el-button>
            </template>
        </el-table-column>
    </el-table>
    </el-col>
    </el-row>
    </el-container>
</template>

<script>
export default {
    props: [
        "isLoading"
    ],
    computed: {
        searchResults: function() {
            return this.$store.getters.searchResults || [];
        }
    },
    methods: {
        fetch: function(searchResult) {
            console.log(searchResult);
            let payload = {
                name: searchResult.name,
                url: searchResult.url
            }
            this.$store.dispatch("fetchDataset", payload);
        }
    }
}
</script>

<style>
.search-flex {
    min-width: 500px;
    max-width: 1000px;
}
</style>
