package errorlinkything;

import java.io.File;

import org.eclipse.core.filesystem.EFS;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IRegion;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.console.IHyperlink;
import org.eclipse.ui.console.IPatternMatchListenerDelegate;
import org.eclipse.ui.console.PatternMatchEvent;
import org.eclipse.ui.console.TextConsole;
import org.eclipse.ui.ide.IDE;
import org.eclipse.ui.texteditor.ITextEditor;

public class ErrorLinkyPatternMatchListenerDelegate implements IPatternMatchListenerDelegate
{
	private TextConsole console;

	@Override
	public void connect(TextConsole console)
	{
		this.console = console;
	}

	@Override
	public void disconnect()
	{
		console = null;
	}

	@Override
	public void matchFound(PatternMatchEvent event)
	{
		try
		{
			String fileReferenceText = console.getDocument().get(event.getOffset(), event.getLength());
			int separatorIndex = fileReferenceText.lastIndexOf(":");

			String absoluteFilePath = fileReferenceText.substring(0, separatorIndex);
			//String workspacePath = ResourcesPlugin.getWorkspace().getRoot().getLocation().toString();

			//if (absoluteFilePath.startsWith(workspacePath))
			{
				//String relativeFilePath = absoluteFilePath.substring(workspacePath.length());
				//IFile file = ResourcesPlugin.getWorkspace().getRoot().getFile(new Path(relativeFilePath));  // this way can work, but only for files in the workspace :(

				int lineNumber = Integer.parseInt(fileReferenceText.substring(separatorIndex + 1));

				//FileLink hyperlink = new FileLink(file, null, -1, -1, lineNumber); // a link to a file in the workspace
				IHyperlink hyperlink = makeHyperlink(absoluteFilePath, lineNumber); // a link to any file
				console.addHyperlink(hyperlink, event.getOffset(), event.getLength());
			}
		}
		catch (Exception exception)
		{
			throw new RuntimeException(exception);
		}
	}

	private static IHyperlink makeHyperlink(String absoluteFilePath, int lineNumber)
	{
		return new IHyperlink()
		{

			@Override
			public void linkExited()
			{
			}

			@Override
			public void linkEntered()
			{
			}

			@Override
			public void linkActivated()
			{
				IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
				try
				{
					IEditorPart editorPart = IDE.openEditorOnFileStore(page, EFS.getStore(new File(absoluteFilePath).toURI()));
					goToLine(editorPart, lineNumber);
				}
				catch (Exception exception)
				{
					throw new RuntimeException(exception);
				}
			}
		};
	}

	private static void goToLine(IEditorPart editorPart, int lineNumber)
	{
		if (editorPart instanceof ITextEditor)
		{
			ITextEditor textEditor = (ITextEditor) editorPart;
			IDocument document = textEditor.getDocumentProvider().getDocument(textEditor.getEditorInput());
			
			if (document != null)
			{
				IRegion region = null;

				try
				{
					region = document.getLineInformation(lineNumber - 1);
				}
				catch (BadLocationException exception)
				{
				}
				
				if (region != null)
					textEditor.selectAndReveal(region.getOffset(), region.getLength());
			}
		}
	}
}