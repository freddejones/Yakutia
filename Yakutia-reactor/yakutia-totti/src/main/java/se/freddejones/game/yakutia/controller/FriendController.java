package se.freddejones.game.yakutia.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import se.freddejones.game.yakutia.entity.Player;
import se.freddejones.game.yakutia.entity.PlayerFriend;
import se.freddejones.game.yakutia.model.dto.FriendDTO;
import se.freddejones.game.yakutia.model.dto.PlayerDTO;
import se.freddejones.game.yakutia.service.FriendService;
import se.freddejones.game.yakutia.service.PlayerService;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

@Controller
@RequestMapping(value = "/friend")
public class FriendController {

    @Autowired
    FriendService friendService;

    @Autowired
    PlayerService playerService;

    @RequestMapping(value  = "/invite", method = RequestMethod.POST)
    @ResponseBody
    public void createNewGame(@RequestBody final FriendDTO friendDTO) {
        friendService.inviteFriend(friendDTO.getPlayerId(), friendDTO.getFriendId());
    }

    @RequestMapping(value  = "/non/friends/{playerId}", method = RequestMethod.GET)
    @ResponseBody
    public List<FriendDTO> getNonFriends(@PathVariable("playerId") Long playerid) {

        // Extract this to the service bean
        List<Player> players = playerService.getAllPlayers();
        Player currentPlayer = playerService.getPlayerById(playerid);

        List<FriendDTO> nonFriends = new ArrayList<FriendDTO>();
        for (Player player : players) {
            if (!(currentPlayer.getPlayerId() == player.getPlayerId()) &&
                    !isPlayerAFriend(currentPlayer.getFriends(), player)) {
                FriendDTO friendDTO = new FriendDTO();
                friendDTO.setPlayerId(player.getPlayerId());
                friendDTO.setPlayerName(player.getName());
                friendDTO.setEmail(player.getEmail());
                nonFriends.add(friendDTO);
            }
        }
        return nonFriends;
    }



    private boolean isPlayerAFriend(Set<PlayerFriend> friends, Player p) {
        Iterator<PlayerFriend> iter = friends.iterator();
        while(iter.hasNext()) {
            PlayerFriend playerFriend = iter.next();
            if (playerFriend.getFriend().getPlayerId() == p.getPlayerId()) {
                return true;
            }
        }
        return false;
    }
}
