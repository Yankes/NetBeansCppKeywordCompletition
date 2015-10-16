/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.netbeans.modules.cppkeywords;

import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.JTextComponent;
import javax.swing.text.StyledDocument;
import org.netbeans.api.editor.mimelookup.MimeLookup;
import org.netbeans.api.editor.mimelookup.MimePath;
import org.netbeans.api.editor.mimelookup.MimeRegistration;
import org.netbeans.api.editor.settings.FontColorSettings;
import org.netbeans.spi.editor.completion.CompletionProvider;
import org.netbeans.spi.editor.completion.CompletionResultSet;
import org.netbeans.spi.editor.completion.CompletionTask;
import org.netbeans.spi.editor.completion.support.AsyncCompletionQuery;
import org.netbeans.spi.editor.completion.support.AsyncCompletionTask;
import org.openide.util.Exceptions;

/**
 *
 * @author Yankes
 */
@MimeRegistration(mimeType = "text/x-c++", service = CompletionProvider.class)
public class CppKeywordsCompletionProvider implements CompletionProvider
{
	private final String[] cpp_keywords = new String[]
	{
		"alignas", "alignof", "and", "and_eq", "asm", "auto",
		"bitand", "bitor", "bool", "break",
		"case", "catch", "char", "char16_t", "char32_t", "class", "compl", "const", "constexpr", "const_cast", "continue",
		"decltype", "default", "delete", "do", "double", "dynamic_cast",
		"else", "enum", "explicit", "export", "extern",
		"false", "float", "for", "friend",
		"goto",
		"if", "inline", "int",
		"long",
		"mutable",
		"namespace", "new", "noexcept", "not", "not_eq", "nullptr",
		"operator", "or", "or_eq",
		"private", "protected", "public",
		"register", "reinterpret_cast", "return",
		"short", "signed", "sizeof", "static", "static_assert", "static_cast", "struct", "switch",
		"template", "this", "thread_local", "throw", "true", "try", "typedef", "typeid", "typename",
		"union", "unsigned", "using",
		"virtual", "void", "volatile",
		"wchar_t", "while",
		"xor", "xor_eq",
		"__VA_ARGS__",
	};

	@Override
	public CompletionTask createTask(int queryType, JTextComponent component)
	{
		if (queryType != CompletionProvider.COMPLETION_ALL_QUERY_TYPE)
			return null;

		return new AsyncCompletionTask(
			new AsyncCompletionQuery()
			{
				@Override
				protected void query(CompletionResultSet resultSet, Document doc, int caretOffset)
				{
					MimePath mimePath = MimePath.parse("text/x-c++");
					FontColorSettings fcs  = (FontColorSettings) MimeLookup.getLookup(mimePath).lookup(FontColorSettings.class);
					try {
						final StyledDocument bDoc = (StyledDocument) doc;
						final int lineStartOffset = bDoc.getParagraphElement(caretOffset).getStartOffset();
						final String line = bDoc.getText(lineStartOffset, caretOffset - lineStartOffset);
						final int whiteOffset = indexOfNotIdentyfer(line);
						String filter = line.substring(whiteOffset + 1);
						int startOffset = lineStartOffset + whiteOffset + 1;

						if(filter.length() > 0)
						{
							for(String item : cpp_keywords)
							{
								if(item.startsWith(filter))
									resultSet.addItem(new CppKeywordsCompletionItem(item, startOffset, caretOffset, fcs.getTokenFontColors("keyword")));
							}
						}
					} catch (BadLocationException ex) {
						Exceptions.printStackTrace(ex);
					}

					resultSet.finish();
				}
			},
			component
		);
	}

	static boolean isIdentyfer(char a)
	{
		return (a >= 'a' && a <= 'z') || (a >= 'A' && a <= 'Z') || (a >= '0' && a <= '9') || (a == '_');
	}

	static int indexOfNotIdentyfer(String line){
		int i = line.length();
		while(--i > -1){
			final char c = line.charAt(i);
			if(!isIdentyfer(c)){
				return i;
			}
		}
		return -1;
	}

	@Override
	public int getAutoQueryTypes(JTextComponent component, String typedText)
	{
		return 0;
	}

}
