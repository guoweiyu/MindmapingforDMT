package edu.agh.klaukold.commands;

import org.xmind.core.IRelationship;

import java.util.LinkedList;
import java.util.Properties;

import edu.agh.klaukold.common.Box;
import edu.agh.klaukold.gui.MainActivity;
import edu.agh.klaukold.interfaces.Command;
import edu.agh.klaukold.utilities.Utils;

/**
 * Created by Klaudia on 2014-11-11.
 */
public class RemoveRelationship implements Command {
    LinkedList<Box> boxes;
    IRelationship relation;
    String s;
    Properties prop;

    @Override
    public void execute(Properties properties) {
        prop = (Properties) properties.clone();
        boxes = (LinkedList<Box>)((LinkedList<Box>) properties.get("boxes")).clone();
      //  IRelationship rel = Utils.findRelationship(boxes.getFirst(), boxes.getLast());
        for (IRelationship rel : boxes.getFirst().relationships.keySet()) {
            if (boxes.getFirst().relationships.get(rel) == boxes.getLast()) {
                relation = rel;
                break;
            }
        }
        MainActivity.sheet1.removeRelationship(relation);
        boxes.getFirst().relationships.remove(relation);
        s = relation.getTitleText();
    }

    @Override
    public void undo() {
        MainActivity.sheet1.addRelationship(relation);
        boxes.getFirst().relationships.put( relation, boxes.getLast());
    }

    @Override
    public void redo() {
        execute(prop);
    }
}
