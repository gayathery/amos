package org.amos.vcs.core;

import java.util.Iterator;

import org.amos.vcs.git.GitClient;
import org.amos.vcs.interfaces.VcsClient;

public class VcsController {
	Environment myEnvironment;
	Boolean isConnected;
	VcsClient vcsClient;
public VcsController(Environment env)
{
	myEnvironment = env;
	isConnected = false;
	}
private VcsController()
{
	}
public void Connect()
{
	Connect(null);
}
public void Connect(String repositoryUri)
{
	switch(myEnvironment)
	{
	case GIT:
		if(repositoryUri != null && !repositoryUri.isEmpty())
		{
			vcsClient = new GitClient(repositoryUri);
			vcsClient.connect();
		}
		break;
	
	}
	
}
public Iterator<String> getBranchList()
{
	return vcsClient.getBranchList();
	}
public Iterator<String> getCommitList()
{
	return vcsClient.getCommitList();
	}
public Iterator<CommitFile> getCommitFiles(String commitID)
{
	return vcsClient.getCommitFiles(commitID);
	
}
}