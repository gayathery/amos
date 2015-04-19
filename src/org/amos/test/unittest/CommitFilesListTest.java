package org.amos.test.unittest;

import java.util.Iterator;

import org.amos.vcs.core.CommitFile;
import org.amos.vcs.core.Environment;
import org.amos.vcs.core.VcsController;

public class CommitFilesListTest {
	public static void main(String args[])
	{
		VcsController base = new VcsController(Environment.GIT);
		base.Connect("D:\\Gayathery_AMOS\\GitHub\\amos-ss15-proj3\\.git");
		Iterator<String> commitList= base.getCommitList();
		try {
			while(commitList.hasNext())
			{
				String commit = commitList.next();
				System.out.println(commit);
				Iterator<CommitFile> fileList=base.getCommitFiles(commit);
				while(fileList.hasNext())
				{
					CommitFile file = fileList.next();
					System.out.println(file.oldPath + ":" +file.newPath+ ":" + file.commitState.name());
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
