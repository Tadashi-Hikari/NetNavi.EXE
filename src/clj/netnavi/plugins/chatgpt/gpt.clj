(ns netnavi.plugins.chatgpt.gpt
  (:import [netnavi.assist Assistant]) 
  (:require [netnavi.assist :as assistant]))
(require 
 '[wkok.openai-clojure.api :as api]
 '[netnavi.assist :as assistant]) 

; These two may not fit here, but was circular dependant in gpt.clj
(def empty-chat [{:role "system" :content "You are a helpful assistant named Sapphire.EXE. your purpose is to help me manage my schedule, projects, and ADHD"}])
(def netnavi-project-assistant-prep [{:role "system" :content "You are a helpful assistant named Sapphire.EXE. You are helping me with a clojure/clojurescript project that I am working on. The project was created using the command \"lein new luminus netnavi +shadow-cljs, +http-kit, +reitit, +sqlite, +graphql\". Most questions will come from this base frame of reference"}])

; This should be moved to GPT Module
(def assistant (Assistant. (atom empty-chat)))

(defn format-prompt [prompt]
  (let [new-map {:role "user" :content prompt}]
    new-map))

(defn format-response [response]
  (let [new-map {:role "assistant" :content response}]
    new-map))

(defn log-prompt-update! [prompt]
  (swap! (:running-log assistant) conj (format-prompt prompt))
  prompt)

(defn log-response-update! [response]
  (swap! (:running-log assistant) conj (format-response response))
  response)

(defn add-new-message [message]
  (let [new-map {:role "user" :content message}]
        (conj empty-chat new-map)))

(defn quick-chat-with-assistant [message]
  (get-in (api/create-chat-completion {:model "gpt-3.5-turbo"
                                       :messages (add-new-message message)}) [:choices 0 :message :content]))

(defn chat-with-assistant 
  "Takes in a string, formats it for GPT, updates the atom! with the string prompt and response. prints response"
  [message]
  (add-new-message (log-prompt-update! message))
  (log-response-update! (get-in (api/create-chat-completion {:model "gpt-3.5-turbo"
                                       :messages @(:running-log assistant)}) [:choices 0 :message :content])))

;(chat-with-assistant "Congratulations. you passed all tests with flying colors")
;(print assistant)