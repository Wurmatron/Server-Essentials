package main

import (
	"encoding/base64"
	"fmt"
	mux "github.com/julienschmidt/httprouter"
	"net/http"
	"strings"
)

type Route struct {
	Name        string
	Method      string
	Pattern     string
	RequireAuth bool
	Handle      mux.Handle
}

type Routes []Route

var routes = Routes{
	// User
	Route{
		"Index",
		"GET",
		"/",
		false,
		Index,
	},
	Route{
		"Validate",
		"GET",
		"/validate",
		true,
		Validate,
	},
	Route{
		"GetUsers",
		"GET",
		"/user/:uuid",
		false,
		GetGlobalUser,
	},
	Route{
		"AddUser",
		"POST",
		"/user/add",
		true,
		SetGlobalUser,
	},
	Route{
		"OverrideUser",
		"PUT",
		"/user/:uuid/override",
		true,
		SetGlobalUser,
	},
	Route{
		"AllUsers",
		"GET",
		"/user",
		false,
		GetAllUsers,
	},
	// Rank
	Route{
		"GetRank",
		"GET",
		"/rank/:name",
		false,
		GetRank,
	},
	Route{
		"SetRank",
		"POST",
		"/rank/add",
		true,
		SetRank,
	},
	Route{
		"OverrideRank",
		"PUT",
		"/rank/:name/override",
		true,
		SetRank,
	},
	Route{
		"AllRanks",
		"GET",
		"/rank",
		false,
		GetAllRanks,
	},
	Route{
		"DelRank",
		"Post",
		"/rank/del",
		false,
		DelRank,
	},
	// AutoRanks
	Route{
		"GetRank",
		"GET",
		"/autoRank/:name",
		false,
		GetAutoRank,
	},
	Route{
		"SetRank",
		"POST",
		"/autoRank/add",
		true,
		SetAutoRank,
	},
	Route{
		"OverrideRank",
		"PUT",
		"/autoRank/:name/override",
		true,
		SetAutoRank,
	},
	Route{
		"AllRanks",
		"GET",
		"/autoRank",
		false,
		GetAllAutoRanks,
	},
	// Team
	Route{
		"GetTeam",
		"GET",
		"/team/:name",
		false,
		GetTeam,
	},
	Route{
		"SetTeam",
		"POST",
		"/team/add",
		true,
		SetTeam,
	},
	Route{
		"OverrideTeam",
		"PUT",
		"/team/:name/override",
		true,
		SetTeam,
	},
	Route{
		"AllTeams",
		"GET",
		"/team",
		false,
		GetAllTeams,
	},
	// Eco (Currency)
	Route{
		"GetCurrency",
		"GET",
		"/eco/:name",
		false,
		GetEco,
	},
	Route{
		"SetCurrency",
		"POST",
		"/eco/add",
		true,
		SetEco,
	},
	Route{
		"OverrideCurrency",
		"PUT",
		"/eco/:name/override",
		true,
		SetEco,
	},
	Route{
		"AllCurrency",
		"GET",
		"/eco",
		false,
		GetAllEco,
	},
	// Status
	Route{
		"GetStatus",
		"GET",
		"/status",
		false,
		GetServerStatus,
	},
	Route{
		"UpdateStatus",
		"POST",
		"/status",
		true,
		PostStatus,
	},
}

func Index(w http.ResponseWriter, _ *http.Request, _ mux.Params) {
	_, _ = fmt.Fprintf(w, "Welcome to the Server-Essentials Rest API")
}

func auth(pass mux.Handle) mux.Handle {
	return func(w http.ResponseWriter, r *http.Request, m mux.Params) {
		auth := strings.SplitN(r.Header.Get("Authorization"), " ", 2)
		if len(auth) != 2 || auth[0] != "Basic" {
			http.Error(w, "Failed to Authorize", http.StatusUnauthorized)
			return
		}
		payload, _ := base64.StdEncoding.DecodeString(auth[1])
		pair := strings.SplitN(string(payload), ":", 2)
		if len(pair) != 2 || !validate(pair[0], pair[1]) {
			http.Error(w, "Failed to Authorize", http.StatusUnauthorized)
			return
		}
		pass(w, r, m)
	}
}

func validate(server, authKey string) bool {
	if server == "server" && authKey == "drowssap" {
		return true
	}
	return false
}
