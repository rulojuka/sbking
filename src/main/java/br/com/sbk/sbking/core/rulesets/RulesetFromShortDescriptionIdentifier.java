package br.com.sbk.sbking.core.rulesets;

import java.util.HashMap;
import java.util.Map;

import br.com.sbk.sbking.core.Strain;
import br.com.sbk.sbking.core.rulesets.abstractClasses.Ruleset;

public class RulesetFromShortDescriptionIdentifier {

	private static Map<String, Ruleset> shortDescriptionOfRulesets = new HashMap<String, Ruleset>();

	// Static initialization block to avoid doing this calculation every time
	// identify(..) is called.
	static {
		for (NegativeRulesetsEnum rulesetEnumElement : NegativeRulesetsEnum.values()) {
			Ruleset current = rulesetEnumElement.getNegativeRuleset();
			shortDescriptionOfRulesets.put(current.getShortDescription(), current);
		}
		for (Strain strain : Strain.values()) {
			Ruleset current = strain.getPositiveRuleset();
			shortDescriptionOfRulesets.put(current.getShortDescription(), current);
		}
	}

	public static Ruleset identify(String gameModeOrStrain) {
		return shortDescriptionOfRulesets.get(gameModeOrStrain);
	}

}
