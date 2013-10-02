package se.freddejones.yakutia.application.service.impl;

import junit.framework.Assert;
import se.freddejones.yakutia.application.service.exceptions.NoPlayerFoundException;
import se.freddejones.yakutia.entity.Game;
import se.freddejones.yakutia.entity.Player;
import se.freddejones.yakutia.application.service.exceptions.PlayerAlreadyExists;
import se.freddejones.yakutia.testcore.JpaTestCase;
import org.junit.Test;

import java.util.List;

public class PreGameBeanTest extends JpaTestCase {

    private String playerName = "PLAYER";
    private String email = "email";
    private Player player;

    @Test
    public void createANewPlayerTest() throws PlayerAlreadyExists {
        PreGameBean preGameBean = new PreGameBean();
        preGameBean.em = entityManager;
        long pId =  preGameBean.createNewPlayer(playerName, "mail@stuff");
        Assert.assertNotNull(pId);
    }

    @Test(expected = PlayerAlreadyExists.class)
    public void playerNameAlreadyExist() throws PlayerAlreadyExists{
        createPlayer();
        PreGameBean preGameBean = new PreGameBean();
        preGameBean.em = entityManager;
        preGameBean.createNewPlayer(playerName, "mail");
    }

    @Test
    public void playerDoesNotExist() {
        PreGameBean preGameBean = new PreGameBean();
        preGameBean.em = entityManager;
        Assert.assertFalse(preGameBean.playerExists("someOtherMail"));
    }

    @Test
    public void playerExistCheckTrue() {
        PreGameBean preGameBean = new PreGameBean();
        preGameBean.em = entityManager;

        // Given: one player with a email
        email = "email";
        createPlayer();

        // When: check if player exists method is called
        boolean doExist = preGameBean.playerExists(email);

        // Then: return true since same email
        Assert.assertTrue(doExist);
    }

    @Test
    public void createANewGameTest() {
        PreGameBean preGameBean = new PreGameBean();
        preGameBean.em = entityManager;

        // Given: a player
        createPlayer();

        // When: creating a game
        long gameId = preGameBean.createNewGame(player.getPlayerId(),"gamename");

        // Then: a game is created
        Assert.assertNotNull(gameId);
    }

    @Test
    public void createNewGameWithAPlayerId() {
        PreGameBean preGameBean = new PreGameBean();
        preGameBean.em = entityManager;

        // Given: a player
        createPlayer();
        long validatePlayerId = player.getPlayerId();

        // When: creating a game
        long gameId = preGameBean.createNewGame(player.getPlayerId(),"gamename2");

        // Then: the game entity contains the player id
        Game g = preGameBean.getGameById(gameId);
        entityManager.refresh(g);
        Assert.assertEquals(validatePlayerId, g.getGameCreatorPlayerId());
    }

    @Test
    public void invitePlayerToGame() {
        PreGameBean preGameBean = new PreGameBean();
        preGameBean.em = entityManager;

        // Given: one player, one game
        createPlayer();
        Game g = new Game();
        entityManager.persist(g);
        entityManager.refresh(g);
        Assert.assertNull(player.getGames());

        // When: player gets an invite
        preGameBean.invitePlayerToGame(player.getPlayerId(), g.getGameId());

        // Then: a game is connected to player
        entityManager.refresh(player);
        Assert.assertTrue(player.getGames().size() == 1);
    }

    @Test
    public void getInvitesNoInvites() {
        PreGameBean preGameBean = new PreGameBean();
        preGameBean.em = entityManager;

        // Given: a player with no invites
        createPlayer();

        // When: checking a invite
        entityManager.refresh(player);
        List<Long> invites = preGameBean.getInvites(player.getPlayerId());

        // Then: no invite for player
        Assert.assertEquals(0, invites.size());
    }

    @Test
    public void getInvitesOneInvite() {
        PreGameBean preGameBean = new PreGameBean();
        preGameBean.em = entityManager;

        // Given: a player with one invite
        createPlayer();
        Game g = new Game();
        entityManager.persist(g);
        entityManager.refresh(g);
        entityManager.refresh(player);
        preGameBean.invitePlayerToGame(player.getPlayerId(),g.getGameId());

        // When: checking a invite
        entityManager.refresh(player);
        List<Long> invites = preGameBean.getInvites(player.getPlayerId());

        // Then: one invite for player
        Assert.assertEquals(1, invites.size());
    }

    @Test
    public void getInvitesOneDeclinedInvite() {
        PreGameBean preGameBean = new PreGameBean();
        preGameBean.em = entityManager;

        // Given: a player with one declined invite
        createPlayer();
        Game g = new Game();
        entityManager.persist(g);
        entityManager.refresh(g);
        entityManager.refresh(player);
        preGameBean.invitePlayerToGame(player.getPlayerId(),g.getGameId());
        preGameBean.declineInvite(player.getPlayerId(),g.getGameId());

        // When: checking invites
        entityManager.refresh(player);
        List<Long> invites = preGameBean.getInvites(player.getPlayerId());

        // Then: one 0 invite for player
        Assert.assertEquals(0, invites.size());
    }

    @Test
    public void acceptTheInviteTest() {
        PreGameBean preGameBean = new PreGameBean();
        preGameBean.em = entityManager;

        // Given: a player and an invite
        createPlayer();
        Game g = new Game();
        entityManager.persist(g);
        entityManager.refresh(g);
        entityManager.refresh(player);
        preGameBean.invitePlayerToGame(player.getPlayerId(), g.getGameId());

        // When: a player accepts the invite
        boolean isAccepted = preGameBean.acceptInvite(player.getPlayerId(), g.getGameId());

        // Then: the invite gets accepted
        Assert.assertTrue(isAccepted);
    }

    @Test
    public void acceptTheInviteButNoInviteExistsTest() {
        PreGameBean preGameBean = new PreGameBean();
        preGameBean.em = entityManager;

        // Given: a player without invite
        createPlayer();
        Game g = new Game();
        entityManager.persist(g);
        entityManager.refresh(g);
        entityManager.refresh(player);

        // When: a player accepts the invite
        boolean isAccepted = preGameBean.acceptInvite(player.getPlayerId(), g.getGameId());

        // Then: the invite does not get accepted
        Assert.assertFalse(isAccepted);
    }

    @Test
    public void declineTheInviteTest() {
        PreGameBean preGameBean = new PreGameBean();
        preGameBean.em = entityManager;

        // Given: a player with a invite
        createPlayer();
        Game g = new Game();
        entityManager.persist(g);
        entityManager.refresh(g);
        entityManager.refresh(player);
        preGameBean.invitePlayerToGame(player.getPlayerId(), g.getGameId());

        // When: a player declines the invite
        boolean isDeclined = preGameBean.declineInvite(player.getPlayerId(), g.getGameId());

        // Then: the invite gets declined
        Assert.assertTrue(isDeclined);
    }

    @Test
    public void declineTheInviteButNoInviteExistsTest() {
        PreGameBean preGameBean = new PreGameBean();
        preGameBean.em = entityManager;

        // Given: a player without a invite
        createPlayer();
        Game g = new Game();
        entityManager.persist(g);
        entityManager.refresh(g);
        entityManager.refresh(player);

        // When: a player declines the invite
        boolean isDeclined = preGameBean.declineInvite(player.getPlayerId(), g.getGameId());

        // Then: the invite does not get declined
        Assert.assertFalse(isDeclined);
    }

    @Test(expected = NoPlayerFoundException.class)
    public void getPlayerByIdNotExistTest() throws NoPlayerFoundException {
        PreGameBean preGameBean = new PreGameBean();
        preGameBean.em = entityManager;

        // Given: a player id that does not exists
        long notExistingId = -999;

        // When: query a player by id
        preGameBean.getPlayerById(notExistingId);

        // Then: exception is thrown
    }

    @Test
    public void getPlayerByIdTest() throws NoPlayerFoundException {
        PreGameBean preGameBean = new PreGameBean();
        preGameBean.em = entityManager;

        // Given: a player
        createPlayer();

        // When: query a player by id
        entityManager.refresh(player);
        Player p = preGameBean.getPlayerById(player.getPlayerId());

        // Then: same player id found
        Assert.assertEquals(player.getPlayerId(), p.getPlayerId());
    }

    @Test(expected = NoPlayerFoundException.class)
    public void getPlayerByNameNotExistTest() throws NoPlayerFoundException {
        PreGameBean preGameBean = new PreGameBean();
        preGameBean.em = entityManager;

        // Given: a playername that does not exists
        String notExistingName = "blahonga";

        // When: query a player by name
        preGameBean.getPlayerByName(notExistingName);

        // Then: exception is thrown
    }

    @Test
    public void getPlayerByNameTest() throws NoPlayerFoundException {
        PreGameBean preGameBean = new PreGameBean();
        preGameBean.em = entityManager;

        // Given: a player
        createPlayer();

        // When: query a player by name
        entityManager.refresh(player);
        Player p = preGameBean.getPlayerByName(playerName);

        // Then: same player id found
        Assert.assertEquals(player.getPlayerId(), p.getPlayerId());
        Assert.assertEquals(player.getName(), p.getName());
    }

    @Test
    public void getPlayerByEmailTest() throws NoPlayerFoundException {
        PreGameBean preGameBean = new PreGameBean();
        preGameBean.em = entityManager;

        // Given: a player
        createPlayer();

        // When: query a player by email
        entityManager.refresh(player);
        Player p = preGameBean.getPlayerByEmail(email);

        // Then: same player id found
        Assert.assertEquals(player.getPlayerId(), p.getPlayerId());
        Assert.assertEquals(player.getName(), p.getName());
        Assert.assertEquals(player.getEmail(), p.getEmail());
    }

    @Test
    public void getPlayersOneExistsTest() {
        PreGameBean preGameBean = new PreGameBean();
        preGameBean.em = entityManager;

        // Given: 1 player exists
        createPlayer();

        // When: query for players
        List<Player> players = preGameBean.getPlayers();

        // Then: list is size of 1
        Assert.assertEquals(1, players.size());
    }

    @Test
    public void getPlayersZeroExists() {
        PreGameBean preGameBean = new PreGameBean();
        preGameBean.em = entityManager;

        // Given: 0 player exists

        // When: query for players
        List<Player> players = preGameBean.getPlayers();

        // Then: list is size of 0
        Assert.assertEquals(0, players.size());
    }

    @Test
    public void getPlayersTwoExists() {
        PreGameBean preGameBean = new PreGameBean();
        preGameBean.em = entityManager;

        // Given: 2 player exists
        createPlayer();
        Player p2 = new Player();
        p2.setName("Blaha");
        entityManager.persist(p2);

        // When: query for players
        List<Player> players = preGameBean.getPlayers();

        // Then: list is size of 2
        Assert.assertEquals(2, players.size());
    }

    @Test
    public void deletePlayerById() throws NoPlayerFoundException {
        // Given: a existing player
        createPlayer();

        // When: deleting a player
        PreGameBean preGameBean = new PreGameBean();
        preGameBean.em = entityManager;

        Player pBefore = preGameBean.getPlayerById(player.getPlayerId());
        Assert.assertNotNull(pBefore);
        preGameBean.deletePlayerById(player.getPlayerId());

        // Then: player is deleted
        try {
            preGameBean.getPlayerById(player.getPlayerId());
        } catch (NoPlayerFoundException npfe) {
            Assert.assertTrue(true);
        }
    }

    @Test(expected = NoPlayerFoundException.class)
    public void testForExceptionWhenGettingEmailThatDoesNotExists() throws NoPlayerFoundException {
        // Given: no player exists (and the bean of course)
        PreGameBean preGameBean = new PreGameBean();
        preGameBean.em = entityManager;

        // When: querying for a player with email test@test.com
        preGameBean.getPlayerByEmail("test@test.com");

        // Then: an exception is thrown (type NoPlayerFoundException)

    }

    private void createPlayer() {
        player = new Player();
        player.setName(playerName);
        player.setEmail(email);
        entityManager.persist(player);
    }
}
