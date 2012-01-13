package org.iplantc.core.client.widgets.factory;

import java.util.List;

import org.iplantc.core.client.widgets.validator.rules.CharacterLimitRule;
import org.iplantc.core.client.widgets.validator.rules.ClipperDataRule;
import org.iplantc.core.client.widgets.validator.rules.DoubleAboveRule;
import org.iplantc.core.client.widgets.validator.rules.DoubleBelowRule;
import org.iplantc.core.client.widgets.validator.rules.DoubleRangeRule;
import org.iplantc.core.client.widgets.validator.rules.FileNameRule;
import org.iplantc.core.client.widgets.validator.rules.GenotypeNameRule;
import org.iplantc.core.client.widgets.validator.rules.IPlantRule;
import org.iplantc.core.client.widgets.validator.rules.IntAboveFieldRule;
import org.iplantc.core.client.widgets.validator.rules.IntAboveRule;
import org.iplantc.core.client.widgets.validator.rules.IntBelowFieldRule;
import org.iplantc.core.client.widgets.validator.rules.IntBelowRule;
import org.iplantc.core.client.widgets.validator.rules.IntRangeRule;
import org.iplantc.core.client.widgets.validator.rules.MustContainRule;
import org.iplantc.core.client.widgets.validator.rules.NonEmptyArrayRule;
import org.iplantc.core.client.widgets.validator.rules.NonEmptyClassRule;
import org.iplantc.core.client.widgets.validator.rules.RegexRule;
import org.iplantc.core.client.widgets.validator.rules.SelectOneCheckboxRule;
import org.iplantc.core.jsonutil.JsonUtil;
import org.iplantc.core.metadata.client.validation.MetaDataRule;

/**
 * Factory class for building validator rules.
 * 
 * @author amuir
 * 
 */
public class IPlantRuleFactory {
    /**
     * Build an IPlantRule from a MetaDataRule.
     * 
     * @param rule template for rule creation.
     * @return new IPlantRule.
     */
    public static IPlantRule build(MetaDataRule rule) {
        IPlantRule ret = null; // assume failure

        if (rule != null) {
            List<String> rules = JsonUtil.buildStringArray(rule.getParams());

            if (rule.getType().equals("IntRange")) {
                ret = new IntRangeRule(rules);
            } else if (rule.getType().equals("IntAbove")) {
                ret = new IntAboveRule(rules);
            } else if (rule.getType().equals("IntBelow")) {
                ret= new IntBelowRule(rules);
            } else if (rule.getType().equals("DoubleRange")) {
                ret = new DoubleRangeRule(rules);
            } else if (rule.getType().equals("DoubleAbove")) {
                ret = new DoubleAboveRule(rules);
            } else if (rule.getType().equals("DoubleBelow")) {
                ret = new DoubleBelowRule(rules);
            } else if (rule.getType().equals("NonEmptyArray")) {
                ret = new NonEmptyArrayRule(rules);
            } else if (rule.getType().equals("NonEmptyClass")) {
                ret = new NonEmptyClassRule(rules);
            } else if (rule.getType().equals("GenotypeName")) {
                ret = new GenotypeNameRule(rules);
            } else if (rule.getType().equals("FileName")) {
                ret = new FileNameRule(rules);
            } else if (rule.getType().equals("IntBelowField")) {
                ret = new IntBelowFieldRule(rules);
            } else if (rule.getType().equals("IntAboveField")) {
                ret = new IntAboveFieldRule(rules);
            } else if (rule.getType().equals("ClipperData")) {
                ret = new ClipperDataRule(rules);
            } else if (rule.getType().equals("MustContain")) {
                ret = new MustContainRule(rules);
            } else if (rule.getType().equals("SelectOneCheckbox")) {
                ret = new SelectOneCheckboxRule(rules);
            } else if (rule.getType().equals("Regex")) {
                ret = new RegexRule(rules);
            } else if (rule.getType().equals("CharacterLimit"))
            {
                ret = new CharacterLimitRule(rules);
            }
        }

        return ret;
    }
}
