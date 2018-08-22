import Vue from 'vue';
import axios from 'axios';
import Constants from '../components/Constants.js';

var chatStore = {
    state: {
        messages: {},
        seenMessageIds: {}
    },
    getters: {
        chatMessages: state => Object.values(state.messages),
        chatTopics: state => {
            let set = new Set(Object.values(state.messages).map(m => m.topic));
            Constants.topics.forEach(t => set.add(t)); //add default topics
            return [...set.values()];
        },
        /**
         * Gets a summary of the number of unseen messages for each topic
         */
        chatTopicUnseenSummary: state => { 
            let allTopics = new Set(Object.values(state.messages).map(m => m.topic));
            Constants.topics.forEach(t => allTopics.add(t)); //add default topics
            allTopics = [...allTopics.values()];
            let unseen = Object.values(state.messages).filter(msg => !Object.keys(state.seenMessageIds).includes(msg.id));
            let summary = {};
            allTopics.forEach(topic => summary[topic] = 0);
            unseen.forEach(msg => summary[msg.topic] += 1);
            return summary;
        },
        chatMessagesByTopic: state => topic => Object.values(state.messages).filter(m => m.topic === topic),
        unSeenMessages: state => {
            let seen = Object.keys(state.seenMessageIds);
            return Object.values(state.messages).filter(msg => !seen.includes(msg.id));
        },
        seenMessages: state => {
            let seen = Object.keys(state.seenMessageIds);
            return Object.values(state.messages).filter(msg => seen.includes(msg.id));
        }
    },
    mutations: {
        updateChatMessages: function (state, messages) {
            messages.forEach(element => {
                Vue.set(state.messages, element.id, element);
            });
        },
        updateSeenMessageIds: function (state, ids){
            ids.forEach((id) => {
                if (typeof state.seenMessageIds[id] === 'undefined'){
                    Vue.set(state.seenMessageIds, id, true);
                }
            })
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