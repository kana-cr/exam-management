    /**
     *  存放 ** 数据
     * **/
    
    // initial state
    const state = {
        all: { 
            Authorization: '',
            username: '',
        }
    }
    
    // getters
    const getters = {}
    
    // actions
    const actions = {}
    
    // mutations
    const mutations = {
        setPrint(state, all) { //设置参数
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