package main

import (
	"encoding/json"
	"fmt"
	"github.com/go-redis/redis"
	mux "github.com/julienschmidt/httprouter"
	"io/ioutil"
	"net/http"
)

var redisDBStatus *redis.Client

func init() {
	redisDBStatus = newClient(redisDatabaseStatus)
}

func Validate(w http.ResponseWriter, _ *http.Request, _ mux.Params) {
	validatedJson, err := json.Marshal(&ValidateResponse{Version: version})
	if err == nil {
		w.Header().Set("Content-Type", "application/json")
		w.WriteHeader(http.StatusOK)
		w.Write(validatedJson)
	}
}

func GetServerStatus(w http.ResponseWriter, _ *http.Request, _ mux.Params) {
	var data []ServerStatus
	for entry := range redisDBStatus.Keys("*").Val() {
		var serverStatus ServerStatus
		json.Unmarshal([]byte(redisDBStatus.Get(redisDBStatus.Keys("*").Val()[entry]).Val()), &serverStatus)
		data = append(data, serverStatus)
	}
	output, err := json.MarshalIndent(data, " ", " ")
	if err != nil || len(output) <= 0 {
		fmt.Fprintln(w, "{}")
		return
	}
	w.Header().Set("Content-Type", "application/json")
	w.Header().Set("version", version)
	if string(output) != "null" {
		fmt.Fprintln(w, string(output))
	} else {
		fmt.Fprintln(w, "{}")
	}
}

func PostStatus(w http.ResponseWriter, r *http.Request, _ mux.Params) {
	b, err := ioutil.ReadAll(r.Body)
	defer r.Body.Close()
	if err != nil {
		http.Error(w, err.Error(), 500)
		return
	}
	var Status ServerStatus
	err = json.Unmarshal(b, &Status)
	if err != nil {
		http.Error(w, err.Error(), 500)
		return
	}
	output, err := json.Marshal(Status)
	if err != nil {
		http.Error(w, err.Error(), 500)
		return
	}
	redisDBStatus.Set(Status.ServerID, output, 180000) // 3 Minutes ( 2 * syncPeriod)
	w.WriteHeader(http.StatusAccepted)
}
