package main

import (
	"encoding/json"
	"flag"
	"fmt"
	"github.com/bwmarrin/discordgo"
	"github.com/google/uuid"
	"io/ioutil"
	"net/http"
	"os"
	"os/signal"
	"strings"
	"syscall"
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
	if strings.Contains("Server Down", "") {
		s.ChannelMessageSend(msg.ChannelID, "<@&396007775655952384>")
	}
	if msg.Author.ID == s.State.User.ID {
		return
	}
	if strings.HasPrefix(msg.Content, "!lookup ") {
		s.ChannelTyping(msg.ChannelID)
		input := strings.Fields(msg.Content)[1]
		if IsValidUUID(input) {
			s.ChannelMessageSend(msg.ChannelID, getUserData(input))
		} else {
			fmt.Println(getUUIDFromUserName(input))
			s.ChannelMessageSend(msg.ChannelID, getUserData(getUUIDFromUserName(input)))
		}
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

func getUUIDFromUserName(name string) string {
	response, err := http.Get(strings.Join([]string{"https://mcapi.de/api/user/", name}, ""))
	if err != nil {
		fmt.Println(err)
		return "The HTTP request failed"
	} else {
		f, _ := ioutil.ReadAll(response.Body)
		data := make(map[string]interface{})
		err := json.Unmarshal(f, &data)
		if err != nil {
			fmt.Println(err)
			return "The HTTP request failed"
		}
		userUUID := data["uuid"].(string)
		return uuid.MustParse(userUUID).String()
	}
}

func IsValidUUID(u string) bool {
	_, err := uuid.Parse(u)
	return err == nil
}
