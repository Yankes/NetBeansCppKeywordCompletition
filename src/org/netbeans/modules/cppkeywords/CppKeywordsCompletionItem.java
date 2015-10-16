/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.netbeans.modules.cppkeywords;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.JTextComponent;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import org.netbeans.api.editor.completion.Completion;
import org.netbeans.spi.editor.completion.CompletionItem;
import org.netbeans.spi.editor.completion.CompletionTask;
import org.netbeans.spi.editor.completion.support.CompletionUtilities;
import org.openide.util.Exceptions;
/**
 *
 * @author Yankes
 */
public class CppKeywordsCompletionItem implements CompletionItem
{
	private final String text;
	private final AttributeSet fieldColor;
	private final int dotOffset;
	private final int caretOffset;

	CppKeywordsCompletionItem(String text, int startoffset, int caretOffset, AttributeSet fieldColor)
	{
		this.text = text;
		this.dotOffset = startoffset;
		this.caretOffset = caretOffset;
		this.fieldColor = fieldColor;
	}

	Font getFont(Font f)
	{
		Boolean bold = Boolean.FALSE, italic = Boolean.FALSE;
		if (fieldColor.isDefined(StyleConstants.Bold))
		{
			bold = (Boolean) fieldColor.getAttribute(StyleConstants.Bold);
		}
		if (fieldColor.isDefined(StyleConstants.Italic))
		{
			italic = (Boolean) fieldColor.getAttribute(StyleConstants.Italic);
		}
		return f.deriveFont((bold.booleanValue() ? Font.BOLD : 0) + (italic.booleanValue() ? Font.ITALIC : 0));
	}

	@Override
	public void defaultAction(JTextComponent component)
	{
		try {
			StyledDocument doc = (StyledDocument) component.getDocument();
			doc.insertString(caretOffset, text.substring(caretOffset-dotOffset), null);
			//This statement will close the code completion box:
			Completion.get().hideAll();
		} catch (BadLocationException ex) {
			Exceptions.printStackTrace(ex);
		}
	}

	@Override
	public void processKeyEvent(KeyEvent evt)
	{

	}

	@Override
	public int getPreferredWidth(Graphics graphics, Font font)
	{
		return CompletionUtilities.getPreferredWidth(text, null, graphics, getFont(font));
	}

	private boolean similarColors(Color a, Color b)
	{
		double diff = Math.pow(a.getBlue() - b.getBlue(),2) + Math.pow(a.getRed()- b.getRed(),2) + Math.pow(a.getGreen()- b.getGreen(),2);
		return diff < 500;
	}

	@Override
	public void render(
		Graphics g, Font defaultFont, Color defaultColor,
		Color backgroundColor, int width, int height, boolean selected)
	{
		Color newColor = StyleConstants.getForeground(fieldColor);
		if(selected || similarColors(newColor, backgroundColor))
			newColor = defaultColor;
		CompletionUtilities.renderHtml(null, text, null, g,
			getFont(defaultFont),
			newColor,
			width, height, selected
		);
	}

	@Override
	public CompletionTask createDocumentationTask()
	{
		return null;
	}

	@Override
	public CompletionTask createToolTipTask()
	{
		return null;
	}

	@Override
	public boolean instantSubstitution(JTextComponent component)
	{
		return false;
	}

	@Override
	public int getSortPriority()
	{
		return 0;
	}

	@Override
	public CharSequence getSortText()
	{
		return text;
	}

	@Override
	public CharSequence getInsertPrefix()
	{
		return text;
	}

}
