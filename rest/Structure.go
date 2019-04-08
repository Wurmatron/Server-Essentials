package main

// General
type ValidateResponse struct {
	Version string `json:"version"`
}

// User
type GlobalUser struct {
	UUID             string             `json:"uuid"`
	Rank             string             `json:"rank"`
	Nick             string             `json:"nick"`
	Bank             Bank               `json:"bank"`
	Team             string             `json:"team"`
	ServerData       []ServerPlayerTime `json:"serverData"`
	Muted            bool               `json:"muted"`
	Language         string             `json:"language"`
	Discord          string             `json:"discord"`
	CustomPermission []string           `json:"customPermission"`
	Perks            []string           `json:"perks"`
}

type Bank struct {
	Coin []Coin `json:"coin"`
}

type Coin struct {
	Name           string  `json:"name"`
	ConversionRate float64 `json:"conversionRate"`
}

type ServerPlayerTime struct {
	ServerID     string  `json:"serverID"`
	OnlineTime   float64 `json:"onlineTime"`
	LastSeen     float64 `json:"lastSeen"`
	FirstJoin    int64   `json:"firstJoin"`
	LoadedChunks int     `json:"loadedChunks"`
}

type AllPlayers struct {
	Players []UserSimple `json:"players"`
}

type UserSimple struct {
	UUID    string `json:"uuid"`
	Rank    string `json:"rank"`
	Discord string `json:"discord"`
	Bank    Bank   `json:"bank"`
}

// Rank
type Rank struct {
	Name        string   `json:"name"`
	Prefix      string   `json:"prefix"`
	Suffix      string   `json:"suffix"`
	Inheritance []string `json:"inheritance"`
	Permission  []string `json:"permission"`
}

// AutoRank
type AutoRank struct {
	PlayTime int32  `json:"playTime"`
	Balance  int64  `json:"balance"`
	Exp      int32  `json:"exp"`
	Rank     string `json:"rank"`
	NextRank string `json:"nextRank"`
}

// Team
type Team struct {
	Name    string   `json:"name"`
	Bank    Bank     `json:"bank"`
	Perks   []string `json:"perks"`
	Owner   string   `json:"owner"`
	Members []string `json:"members"`
}

// Eco / Currency
type Currency struct {
	Name string  `json:"name"`
	Sell float64 `json:"sell"`
	Buy  float64 `json:"buy"`
}

// Server Data
type ServerStatus struct {
	Name       string   `json:"name"`
	Status     string   `json:"status"`
	Time       float64  `json:"time"`
	Players    []string `json:"players"`
	LastUpdate float64  `json:"lastUpdate"`
	Version    string   `json:"version"`
}
