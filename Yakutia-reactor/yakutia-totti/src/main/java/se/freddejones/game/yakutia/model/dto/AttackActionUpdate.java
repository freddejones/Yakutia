package se.freddejones.game.yakutia.model.dto;

public class AttackActionUpdate {

    private String territoryAttackSrc;
    private String territoryAttackDest;
    private int attackingNumberOfUnits;

    public String getTerritoryAttackSrc() {
        return territoryAttackSrc;
    }

    public void setTerritoryAttackSrc(String territoryAttackSrc) {
        this.territoryAttackSrc = territoryAttackSrc;
    }

    public String getTerritoryAttackDest() {
        return territoryAttackDest;
    }

    public void setTerritoryAttackDest(String territoryAttackDest) {
        this.territoryAttackDest = territoryAttackDest;
    }

    public int getAttackingNumberOfUnits() {
        return attackingNumberOfUnits;
    }

    public void setAttackingNumberOfUnits(int attackingNumberOfUnits) {
        this.attackingNumberOfUnits = attackingNumberOfUnits;
    }
}
