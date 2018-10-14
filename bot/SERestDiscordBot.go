package main

import (
	"bytes"
	"flag"
	"fmt"
	"io/ioutil"
	"net/http"
	"os"
	"os/signal"
	"strings"
	"syscall"

	"github.com/bwmarrin/discordgo"
)

var (
	Token string
	Url   string
)

func init() {
	flag.StringVar(&Token, "t", "", "Bot Token")
	flag.StringVar(&Url, "w", "", "Rest URL")
	flag.Parse()
}

func main() {
	discord, err := discordgo.New("Bot " + Token)
	if err != nil {
		fmt.Println("error creating Discord session,", err)
		return
	}
	discord.AddHandler(messageCreate)

	err = discord.Open()
	if err != nil {
		fmt.Println("error opening connection,", err)
		return
	}

	fmt.Println("SE Discord Bot is now running.  Press CTRL-C to exit.")
	sc := make(chan os.Signal, 1)
	signal.Notify(sc, syscall.SIGINT, syscall.SIGTERM, os.Interrupt, os.Kill)
	<-sc

	discord.Close()
}

func messageCreate(s *discordgo.Session, msg *discordgo.MessageCreate) {
	if msg.Author.ID == s.State.User.ID {
		return
	}

	if strings.HasPrefix(msg.Content, "!lookup ") {
		s.ChannelTyping(msg.ChannelID)
		fmt.Println(msg.Content[9:45])
		s.ChannelMessageSend(msg.ChannelID, getUserData(msg.Content[9:45]))
	}
}

func getUserData(uuid string) string {
	response, err := http.Get(strings.Join([]string{Url, "/user/find/", uuid}, ""))
	if err != nil {
		fmt.Println(err)
		return "The HTTP request failed"
	} else {
		data, _ := ioutil.ReadAll(response.Body)
		return string(data)
	}
}

func postUserData(uuid string, json string) string {
	response, err := http.Post(strings.Join([]string{Url, "/user/add/"}, ""), "application/json", bytes.NewBufferString(json))
	if err != nil {
		return "The HTTP post request failed"
	} else {
		data, _ := ioutil.ReadAll(response.Body)
		return string(data)
	}
}
