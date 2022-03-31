package com.twsbrian.MobDrop2.Command.CommandsList;

import java.util.List;

public class CommandsTools {
	public void search(List<String> inputlist , String check, List<String> outputlist) {
		for(int loopnum1 = 0 ; loopnum1 < inputlist.size();loopnum1++) {
			
			//if(inputlist.get(loopnum1).indexOf(check) != -1) {
			if(inputlist.get(loopnum1).startsWith(check)) {
				outputlist.add(inputlist.get(loopnum1));
			}
		}
	}
}
