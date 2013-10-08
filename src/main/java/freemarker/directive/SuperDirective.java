package freemarker.directive;

import freemarker.core.Environment;
import freemarker.directive.OverrideDirective.TemplateDirectiveBodyOverrideWraper;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;

import java.io.IOException;
import java.util.Map;

/**
 * 类功能
 * <p/>
 * User: baitao.jibt@gmail.com
 * Date: 12-7-8
 * Time: 上午9:44
 */
public class SuperDirective implements TemplateDirectiveModel {

  public final static String DIRECTIVE_NAME = "super";

  public void execute(Environment env,
                      Map params, TemplateModel[] loopVars,
                      TemplateDirectiveBody body) throws TemplateException, IOException {
    TemplateDirectiveBodyOverrideWraper current = (TemplateDirectiveBodyOverrideWraper) env.getVariable(DirectiveUtils.OVERRIDE_CURRENT_NODE);
    if (current == null) {
      throw new TemplateException("<@super/> direction must be child of override", env);
    }
    TemplateDirectiveBody parent = current.parentBody;
    if (parent == null) {
      throw new TemplateException("not found parent for <@super/>", env);
    }
    parent.render(env.getOut());
  }

}
