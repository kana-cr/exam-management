    /**
     *  存放 ** 数据
     * **/
    
    // initial state
    const state = {
        all: { 
            userId: "",
        }
    }
    
    // getters
    const getters = {}
    
    // actions
    const actions = {}
    
    // mutations
    const mutations = {
        setUserId(state, all) { //设置参数
            state.all = all;
        }
    }
    
    export default {
        namespaced: true,
        state,
        getters,
        actions,
        mutations
    }