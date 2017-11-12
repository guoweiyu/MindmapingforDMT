package edu.agh.klaukold.commands;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;

import org.xmind.core.ITopic;
import org.xmind.core.ITopicPath;
import org.xmind.core.style.IStyle;
import org.xmind.core.style.IStyleSheet;
import org.xmind.ui.style.Styles;

import java.util.Properties;

import edu.agh.R;
import edu.agh.klaukold.common.Box;
import edu.agh.klaukold.common.Line;
import edu.agh.klaukold.common.Point;
import edu.agh.klaukold.enums.Position;
import edu.agh.klaukold.gui.MainActivity;
import edu.agh.klaukold.interfaces.Command;

public class AddBox implements Command {
    public Box box;
    Properties before;
    Properties after;
    Box parent;
    public String name;
    Line line;
	@Override
	public void execute(Properties properties) {
        before = (Properties)properties.clone();
        after = (Properties)properties.clone();
        parent = (Box)properties.get("box");
        box = (Box) properties.get("new_box");
        box.parent = parent;
        ITopic topic = MainActivity.workbook.createTopic();
        IStyle topicStyle = MainActivity.styleSheet.createStyle(IStyle.TOPIC);
        MainActivity.styleSheet.addStyle(topicStyle, IStyleSheet.NORMAL_STYLES);
        topic.setStyleId(topicStyle.getId());
        box.topic = topic;
       // box.topic.setFolded(true);
        parent.topic.add(box.topic);
        String style = (String) properties.get("style");
        Resources res = (Resources) properties.get("res");
        parent.addChild(box);
        box.setHeight(100);

         if (parent.topic.isRoot() && ( style.equals("Classic") || (MainActivity.sheet1.getTheme() != null && MainActivity.sheet1.getTheme().getName().equals("%classic"))) ) {
            box.setDrawableShape((GradientDrawable) res.getDrawable(R.drawable.round_rect));
           topicStyle.setProperty(Styles.ShapeClass, Styles.TOPIC_SHAPE_ROUNDEDRECT);
           topicStyle.setProperty(Styles.FillColor, "#CCE5FF");
        } else if (parent.topic.isRoot() && ( style.equals("Simple") || (MainActivity.sheet1.getTheme() != null && MainActivity.sheet1.getTheme().getName().equals("%simple") )))  {
            box.setDrawableShape((GradientDrawable) res.getDrawable(R.drawable.no_border));
                topicStyle.setProperty(Styles.ShapeClass, Styles.TOPIC_SHAPE_NO_BORDER);
        } else if (parent.topic.isRoot() && (style.equals("Business") || (MainActivity.sheet1.getTheme() != null && MainActivity.sheet1.getTheme().getName().equals("%business") )))   {
            box.setDrawableShape((GradientDrawable) res.getDrawable(R.drawable.rect));
                topicStyle.setProperty(Styles.ShapeClass, Styles.TOPIC_SHAPE_RECT);
                topicStyle.setProperty(Styles.FillColor, "#FFFFFF");
        } else if (parent.topic.isRoot() && (style.equals("Academese")|| (MainActivity.sheet1.getTheme() != null && MainActivity.sheet1.getTheme().getName().equals("%academese") )))  {
            box.setDrawableShape((GradientDrawable) res.getDrawable(R.drawable.elipse));
                topicStyle.setProperty(Styles.ShapeClass, Styles.TOPIC_SHAPE_ELLIPSE);
                topicStyle.setProperty(Styles.FillColor, "#404040");
        }        else  if (parent.topic.isRoot() && ( style.equals("Default") || (MainActivity.sheet1.getTheme() == null ) )) {
                box.setDrawableShape((GradientDrawable) res.getDrawable(R.drawable.round_rect));

            }
            else {
            if (!parent.topic.isRoot()) {
                box.setWidth(70);
                box.setHeight(50);
            }
            box.setDrawableShape((GradientDrawable) res.getDrawable(R.drawable.round_rect));
        }
        parent.isExpendable = true;
        if (box.point != null) {
            Line line1;
            Point linePoint1;
            Point linePoint2;
            if (box.point.x < MainActivity.root.drawableShape.getBounds().centerX()) {
                linePoint2 =  new Point(box.point.x + box.getWidth(), box.point.y + box.getHeight()/2);
                linePoint1 =  new Point(parent.getDrawableShape().getBounds().left, parent.getDrawableShape().getBounds().centerY());
            } else {
                linePoint2 =  new Point(box.point.x, box.point.y + box.getHeight()/2);
                linePoint1 =  new Point(parent.getDrawableShape().getBounds().right, parent.getDrawableShape().getBounds().centerY());

            }

            if (parent.topic.getStyleId() != null ) {
                String lineClass =  MainActivity.styleSheet.findStyle(parent.topic.getStyleId()).getProperty(Styles.LineClass);
                String width = MainActivity.styleSheet.findStyle(parent.topic.getStyleId()).getProperty(Styles.LineWidth);
                if (width == null) {
                    width = "1";
                } else {
                    width = width.replace("pt", "");
                }
                if (MainActivity.styleSheet.findStyle(parent.topic.getStyleId()).getProperty(Styles.LineColor) != null) {
                    line1 = new Line(lineClass, Integer.parseInt(width), new ColorDrawable(Color.parseColor(MainActivity.styleSheet.findStyle(parent.topic.getStyleId()).getProperty(Styles.LineColor))), linePoint1, linePoint2, true);
                } else {
                    line1 = new Line(lineClass, Integer.parseInt(width), new ColorDrawable(Color.GRAY), linePoint1, linePoint2, true);
                }
            } else {
                line1 = new Line(null, 1, new ColorDrawable(Color.GRAY), linePoint1, linePoint2, true);
            }
            parent.getLines().put(box, line1);
            if (name != null) {
                box.topic.setTitleText(name);
            }
        }
	}

    @Override
    public void undo() {
        parent.topic.remove(box.topic);
        parent.getLines().remove(box);
        parent.getChildren().remove(box);

    }

    @Override
    public void redo() {
        execute(after);
    }

}
