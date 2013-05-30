package nu.danielsundberg.yakutia.base;

import de.agilecoders.wicket.markup.html.bootstrap.behavior.BootstrapBaseBehavior;
import de.agilecoders.wicket.markup.html.bootstrap.block.Code;
import de.agilecoders.wicket.markup.html.bootstrap.html.ChromeFrameMetaTag;
import de.agilecoders.wicket.markup.html.bootstrap.html.HtmlTag;
import de.agilecoders.wicket.markup.html.bootstrap.html.MetaTag;
import de.agilecoders.wicket.markup.html.bootstrap.html.OptimizedMobileViewportMetaTag;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import java.util.logging.Logger;

/**
 * User: Fredde
 * Date: 3/26/13 10:54 PM
 */
public class BasePage extends WebPage {

    public BasePage(final PageParameters parameters) {
        super(parameters);

        add(new HtmlTag("html"));
        add(new OptimizedMobileViewportMetaTag("viewport"));
        add(new ChromeFrameMetaTag("chrome-frame"));
        add(new MetaTag("description", Model.of("description"), Model.of("Yakutia game")));
        add(new MetaTag("author", Model.of("author"), Model.of("Fredrik Jones <freddejones@gmail.com>")));
        add(new BootstrapBaseBehavior());
        add(new Code("code-internal"));

        add(makeVersionLabel());
    }

    private Label makeVersionLabel() {
        return new Label("version", makeVersionValue());
    }

    private String makeVersionValue() {
        return getApplication().getFrameworkSettings().getVersion();
    }

}
