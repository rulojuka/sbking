package br.com.sbk.sbking.core;

import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;

public final class ScoreCalculator {

    /**
     * This is governed by LAW 77 - DUPLICATE BRIDGE SCORING TABLE
     */

    private static final Map<Strain, Integer> TRICK_SCORE_UNDOUBLED;
    private static final int NO_TRUMP_FIRST_TRICK_BONUS;

    private ScoreCalculator() {
        // Static class. Should not be instantiated
    }

    static {
        NO_TRUMP_FIRST_TRICK_BONUS = 10;
        Map<Strain, Integer> modifiableMap = new EnumMap<Strain, Integer>(Strain.class);
        modifiableMap.put(Strain.CLUBS, 20);
        modifiableMap.put(Strain.DIAMONDS, 20);
        modifiableMap.put(Strain.HEARTS, 30);
        modifiableMap.put(Strain.SPADES, 30);
        modifiableMap.put(Strain.NOTRUMPS, 30);
        TRICK_SCORE_UNDOUBLED = Collections.unmodifiableMap(modifiableMap);
    }

    public static int score(Contract contract, int tricks) {
        if (tricks < 0 || tricks > 13) {
            throw new IllegalArgumentException();
        }
        int overOrUnderTricks = tricks - 6 - contract.getLevel();
        if (overOrUnderTricks >= 0) {
            return contractMadeScore(contract, overOrUnderTricks);
        } else {
            return -contractFailedScore(contract, -overOrUnderTricks);
        }
    }

    private static int contractMadeScore(Contract contract, int overtricks) {
        int penaltyMultiplier = getPenaltyMultiplier(contract);
        int trickScore = contract.getLevel() * penaltyMultiplier * getTrickScoreUndoubled(contract.getStrain());
        if (Strain.NOTRUMPS.equals(contract.getStrain())) {
            trickScore += NO_TRUMP_FIRST_TRICK_BONUS * penaltyMultiplier;
        }
//        System.out.println("Contract: " + contract);
//        System.out.println("trickscore: " + trickScore);
        boolean isGame = trickScore >= 100;
        int premiumScore = getPremiumScore(contract, isGame);
//        System.out.println("premiumScore: " + premiumScore);
        int overtrickBonus = getOvertrickBonus(contract, overtricks);
//        System.out.println("overtrickBonus: " + overtrickBonus);
//        System.out.println();
        return trickScore + premiumScore + overtrickBonus;
    }

    private static int getTrickScoreUndoubled(Strain strain) {
        return TRICK_SCORE_UNDOUBLED.get(strain);
    }

    private static int getPenaltyMultiplier(Contract contract) {
        if (contract.getDoubled()) {
            return 2;
        }
        if (contract.getRedoubled()) {
            return 4;
        }
        return 1;
    }

    private static int getPremiumScore(Contract contract, boolean isGame) {
        return getPremiumScoreGrandSlam(contract) + getPremiumScoreSmallSlam(contract) + getPremiumScoreGameOrPartscore(contract, isGame)
                + getPremiumScoreDoubled(contract) + getPremiumScoreRedoubled(contract);
    }

    private static int getPremiumScoreGrandSlam(Contract contract) {
        if (contract.getLevel() == 7) {
            if (contract.isVulnerable()) {
                return 1500;
            } else {
                return 1000;
            }
        }
        return 0;
    }

    private static int getPremiumScoreSmallSlam(Contract contract) {
        if (contract.getLevel() == 6) {
            if (contract.isVulnerable()) {
                return 750;
            } else {
                return 500;
            }
        }
        return 0;
    }

    private static int getPremiumScoreGameOrPartscore(Contract contract, boolean isGame) {
        if (isGame) {
            if (contract.isVulnerable()) {
                return 500;
            } else {
                return 300;
            }
        }
        return 50;
    }

    private static int getPremiumScoreDoubled(Contract contract) {
        if (contract.getDoubled()) {
            return 50;
        }
        return 0;
    }

    private static int getPremiumScoreRedoubled(Contract contract) {
        if (contract.getRedoubled()) {
            return 100;
        }
        return 0;
    }

    private static int getOvertrickBonus(Contract contract, int overtricks) {
        int baseValue = TRICK_SCORE_UNDOUBLED.get(contract.getStrain());
        if (contract.getDoubled()) {
            baseValue = 100;
            if (contract.isVulnerable()) {
                baseValue *= 2;
            }
        } else if (contract.getRedoubled()) {
            baseValue = 200;
            if (contract.isVulnerable()) {
                baseValue *= 2;
            }
        }
        return overtricks * baseValue;
    }

    private static int contractFailedScore(Contract contract, int undertricks) {
        if (undertricks <= 0) {
            return 0;
        }
        if (contract.isVulnerable()) {
            if (contract.getDoubled()) {
                return (undertricks * 300) - 100;
            } else if (contract.getRedoubled()) {
                return (undertricks * 600) - 200;
            } else {
                return undertricks * 100;
            }
        } else {
            if (contract.getDoubled()) {
                if (undertricks < 4) {
                    return (undertricks * 200) - 100;
                } else {
                    return (undertricks * 300) - 400;
                }
            } else if (contract.getRedoubled()) {
                if (undertricks < 4) {
                    return (undertricks * 400) - 200;
                } else {
                    return (undertricks * 600) - 800;
                }
            } else {
                return undertricks * 50;
            }
        }

    }

    // private class TricksMade{
    // private int tricks;
    // public TricksMade(int tricks) {
    // if(tricks<0 || tricks > 13) {
    // throw new IllegalArgumentException();
    // }
    // this.tricks = tricks;
    // }
    // public int getTricks() {
    // return tricks;
    // }
    // }

}
