script_directory = File.dirname(__FILE__)
require File.join(script_directory,"SuperUtilities.jar")
java_import com.nuix.superutilities.SuperUtilities
$su = SuperUtilities.init($utilities,NUIX_VERSION)
require File.join(script_directory,"TermExplorerGUI.jar")
java_import com.nuix.termexplorer.MainPanel

panel = MainPanel.new($window,$current_case)
$window.addTab("Term Explorer",panel)