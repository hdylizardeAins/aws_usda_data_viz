<template>
    <div id="visualization-panel">
        <el-container>
            <el-row>3. View and tailor Visualizations</el-row>
            <el-row>
                <el-col>
                    <el-tabs v-model="editableTabsValue2" type="card" @tab-remove="removeTab">
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
            <!-- <el-row>
                <el-col :offset="22" >
                    <el-button type="primary" :disabled="nextBtnDisabled" >Next</el-button>
                </el-col>
            </el-row> -->
        </el-container>
    </div>
</template>

<script>
export default {
    data() {
        return {
            editableTabsValue2: '1',
            tabIndex: 1
        }
    },
    computed: {
        selectedAnalytics: function(){
            return this.$store.getters.selectedAnalytics;
        },
        editableTabs2: function(){
            
            if (this.selectedAnalytics.length == 0){
                
                return //TODO: this isn't showing up in UI even though the block is entere
                    [
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
        addTab(targetName) {
            let newTabName = ++this.tabIndex + '';
            this.editableTabs2.push({
                title: 'New Tab',
                name: newTabName,
                content: 'New Tab content'
                });
            this.editableTabsValue2 = newTabName;
        },
        removeTab(targetName) {
            let tabs = this.editableTabs2;
            let activeName = this.editableTabsValue2;
            if (activeName === targetName) {
                tabs.forEach((tab, index) => {
                    if (tab.name === targetName) {
                        let nextTab = tabs[index + 1] || tabs[index - 1];
                        if (nextTab) {
                            activeName = nextTab.name;
                        }
                    }
                });
        }
        
        this.editableTabsValue2 = activeName;
        this.editableTabs2 = tabs.filter(tab => tab.name !== targetName);
      }
    }
};
</script>
<style>
</style>
