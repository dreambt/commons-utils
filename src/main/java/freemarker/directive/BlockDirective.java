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
 * Time: 上午9:38
 */
public class BlockDirective implements TemplateDirectiveModel {

  public final static String DIRECTIVE_NAME = "block";

  public void execute(Environment env,
                      Map params, TemplateModel[] loopVars,
                      TemplateDirectiveBody body) throws TemplateException, IOException {
    String name = DirectiveUtils.getRequiredParam(params, "name");
    TemplateDirectiveBodyOverrideWraper overrideBody = DirectiveUtils.getOverrideBody(env, name);
    if (overrideBody == null) {
      if (body != null) {
        body.render(env.getOut());
      }
    } else {
      DirectiveUtils.setTopBodyForParentBody(env, new TemplateDirectiveBodyOverrideWraper(body, env), overrideBody);
      overrideBody.render(env.getOut());
    }
  }

}
