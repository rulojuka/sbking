package core.rulesets;

public class PositiveNoTrumpsRuleset extends PositiveRuleset{

	@Override
	public String getShortDescription() {
		return "Positive no trumps";
	}

	@Override
	public String getCompleteDescription() {
		return "Make the most tricks without a trump suit";
	}

}
