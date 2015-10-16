/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.netbeans.modules.cppkeywords;

import org.netbeans.api.editor.mimelookup.MimeRegistration;
import org.netbeans.spi.editor.completion.CompletionProvider;

/**
 *
 * @author Yankes
 */
@MimeRegistration(mimeType = "text/x-h", service = CompletionProvider.class)
public class CppKeywordsCompletionHeaderProvider extends CppKeywordsCompletionProvider
{

}
