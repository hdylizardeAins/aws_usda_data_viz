import Vue from 'vue';
import axios from 'axios';

var chatStore = {
    state: {
        messages: []
    },
    getters: {
        chatTopics: state => [...new Set(state.messages.map(m => m.topic))],
        chatMessagesByTopic: state => topic => state.messages.filter(m => m.topic === topic)
    },
    mutations: {
        updateChatMessages: function (state, messages) {
            let count = 0;
            messages.forEach(element => {
                Vue.set(state.messages, count, element);
                count++;
            });
        }
    },
    actions: {
        loadChatMessages: function (context, payload){
            axios.get('/chat')
                .then(function (response) {
                    context.commit("updateChatMessages", response.data);
                    if (typeof (payload.success) === 'function') {
                        payload.success(response);
                    }
                })
                .catch(function (error) {
                    if (typeof (payload.error) === 'function') {
                        payload.failure(error);
                    }
                })
        },
        postChatMessage: function (context, payload){
            axios.post('/chat', payload.data)
                .then(function (response) {
                    if (typeof (payload.success) === 'function') {
                        payload.success(response);
                    }
                })
                .catch(function (error) {
                    if (typeof (payload.error) === 'function') {
                        payload.failure(error);
                    }
                })
        }
    }
};

export default chatStore;