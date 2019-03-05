/*

TENTE := TErms Negated on TExt

In order for TENTE to work, the "building blocks" 'CUTEXT' and 'Modifier' must be installed

*/


package tente.main;

import cutext.util.Estaticos;

import java.io.*;
import java.nio.charset.StandardCharsets;

import java.util.*;
import java.util.regex.*;



public class Main implements Serializable
{

	private static final long serialVersionUID = -7149755349268484907L;



	//info
	private static final String HELP = "-help"; //Show this message
	private static final String DISON = "-displayon"; //Show the messages at the standard output. Default TRUE (show)
	
	//params
	private static final String LAT = "language"; //SPANISH or ENGLISH. Default SPANISH
	private static final String EC = "-execCutext"; //Extract terms with cutext (true) or not (false). Default TRUE

	//routes
	private static final String TEXT = "-text"; // Input file with this format: id|sentence. Default at ../in/text.txt
	private static final String TRM = "-terms"; //Name of the input file with terms or empty (depends on parameter execCutext is false or true). Default: at ../in/terms.txt
	private static final String TEMP = "-temporary"; // Temporary folder. Default at ../temp/
	//
	private static final String OUTFO = "-outputFolder"; //Name of the output folder. Default: at ../out/
	private static final String OUTFI = "-outputFile"; //Name of the output file. Default: 'negTerms.txt'
	
	
	boolean dison = true;

    String language = cutext.prepro.DatosEntrada.SPA;
	boolean execCutext = true;
	
	String text = "../in/text.txt";
	String terms = "../in/terms.txt";
	String temporary = "../temp/";
	//
	String outputFolder = "../out/";
	String outputFile = "negTerms.txt";
	
	
	//this files are at temporary folder
	String inputCutext = "toCutext.txt";
	String inputModifier = "toModifier.txt";
	String outModifier = "outModifier.txt";
				
	List<String> listText = null;
	List<String> listTerms = null;
    
    //
    //cutext
	private String cutextProperties = 
											".." + 
											Estaticos.FILE_SEP + 
											"config" + 
											Estaticos.FILE_SEP + 
											"cutext.properties";
	//modifier
	private String modifierProperties = 
											".." + 
											Estaticos.FILE_SEP + 
											"config" + 
											Estaticos.FILE_SEP + 
											"modifier.properties";
											



	
	
	cutext.prepro.DatosEntrada cutextData;
	String cutextArgs[];
	
    //
	String routeconfigfilescutext = 
								".." + 
								Estaticos.FILE_SEP + 
								".." + 
								Estaticos.FILE_SEP + 
                                "cutext" + 
								Estaticos.FILE_SEP + 
								"config_files" + 
								Estaticos.FILE_SEP;
	
	String routeinternttcutext = 
								".." + 
								Estaticos.FILE_SEP + 
								".." + 
                                Estaticos.FILE_SEP + 
                                "cutext" + 
								Estaticos.FILE_SEP + 
								"intern" + 
								Estaticos.FILE_SEP + 
								"TT" + 
								Estaticos.FILE_SEP;
	
	String routeHashTermsCutext = 
								".." + 
								Estaticos.FILE_SEP + 
								".." + 
								Estaticos.FILE_SEP + 
                                "cutext" + 
								Estaticos.FILE_SEP + 
								"out" + 
								Estaticos.FILE_SEP + 
								"serHashTerms" + 
								Estaticos.FILE_SEP;
	
	String routeTextFileHashTermsCutext = 
								".." + 
								Estaticos.FILE_SEP + 
								".." + 
								Estaticos.FILE_SEP + 
                                "cutext" + 
								Estaticos.FILE_SEP + 
								"out" + 
								Estaticos.FILE_SEP + 
								"fileTextHashTerms" + 
								Estaticos.FILE_SEP;
    
    

	
	
	
	public Main(String args[])
	{
		this.text = this.text.replace("/", Estaticos.FILE_SEP);
		this.terms = this.terms.replace("/", Estaticos.FILE_SEP);
		this.temporary = this.temporary.replace("/", Estaticos.FILE_SEP);
		//
		this.outputFolder = this.outputFolder.replace("/", Estaticos.FILE_SEP);
		this.outputFile = this.outputFile.replace("/", Estaticos.FILE_SEP);
		startMain(args);
	}


	public static void main(String args[]) throws IOException
	{
		Main c = new Main(args);
	}
	
	
		/**
	* Starts the text mode interface.
	*  
	* @param args the commandline arguments.
	*/
	public void startMain(String[] args)
	{
		for(int i = 0; i < args.length; i++)
		{
			if(args[i].equals(HELP))
			{
				usage();
			}
			//
			else if(args[i].equals(DISON))
			{
				i++;
				if(i >= args.length)
				{
					usage();
				}
				this.dison = Boolean.parseBoolean(args[i].toLowerCase());
			}
			//
			else if(args[i].equals(LAT))
			{
				i++;
				if(i >= args.length)
				{
					usage();
				}
				this.language = args[i].toUpperCase();
				if(!this.language.equals(cutext.prepro.DatosEntrada.SPA) || !this.language.equals(cutext.prepro.DatosEntrada.ENG))
				{
					System.err.println("The selected language can only be SPANISH or ENGLISH");
					System.exit(1);
				}
			}
			else if(args[i].equals(EC))
			{
				i++;
				if(i >= args.length)
				{
					usage();
				}
				this.execCutext = Boolean.parseBoolean(args[i].toLowerCase());
			}
            //
			else if(args[i].equals(TEXT))
			{
				i++;
				if(i >= args.length)
				{
					usage();
				}
				this.text = args[i];
			}
			else if(args[i].equals(TRM))
			{
				i++;
				if(i >= args.length)
				{
					usage();
				}
				this.terms = args[i];
			}
			else if(args[i].equals(TEMP))
			{
				i++;
				if(i >= args.length)
				{
					usage();
				}
				this.temporary = args[i];
			}
			//
			else if(args[i].equals(OUTFO))
			{
				i++;
				if(i >= args.length)
				{
					usage();
				}
				this.outputFolder = args[i];
			}
			else if(args[i].equals(OUTFI))
			{
				i++;
				if(i >= args.length)
				{
					usage();
				}
				this.outputFile = args[i];
			}
			//
			else //other parameter
			{
				System.err.println("The parameter " + args[i] + " is not among the possibilities offered");
				System.exit(0);
			}
		}
		exec();
	}
	
	
	


	public void exec()
	{
        //time
        cutext.util.Tiempo t = new cutext.util.Tiempo();
        long itime = System.nanoTime();
        
        //check if the input files exist
		checkInputFiles();

		
		//
		if(this.dison)
			System.out.println("\nIn action... ");
		execution();
		if(this.dison)
			System.out.println("\nEnd of execution.");
        //
        
        //time
        long ftime = System.nanoTime() - itime;
		t = new cutext.util.Tiempo();
		t.conversion(ftime);
		System.out.println("\nTime:" + t.aString());
	}
	
	public void checkInputFiles()
	{
		try
		{
			//text
			File f = new File(this.text);
			if(!f.isFile())
				f.createNewFile();
			//terms
			f = new File(this.terms);
			if(!f.isFile())
				f.createNewFile();			
		}
		catch(IOException e)
		{
			System.out.println("Error: " + e);
			e.printStackTrace();
		}
	}
	
	public void createOutputFile()
	{
		try
		{
			//output
			File f = new File(this.outputFolder + this.outputFile);
			if(!f.isFile())
				f.createNewFile();
		}
		catch(IOException e)
		{
			System.out.println("Error: " + e);
			e.printStackTrace();
		}
	}
    
    public void execution()
    {
        this.listText = 
			cutext.util.FileToString.asStringList(this.text, StandardCharsets.UTF_8);
		
		//check listText
		if(listText.isEmpty())
		{
			System.err.println("The input file " + this.text + " is empty. Nothing to do.");
			System.exit(1);
		}
		
		//delete temporary and output files
		if(this.dison)
			System.out.println("\nDeleting temporary and output files...\n");
		cutext.util.Estaticos.deleteFiles(this.temporary);
		cutext.util.Estaticos.deleteFiles(this.outputFolder);
		//create output file
		createOutputFile();
			
		this.listTerms = 
			cutext.util.FileToString.asStringList(this.terms, StandardCharsets.UTF_8);

			
		if(this.execCutext)
		{
			//empty the terms file
			try
			{
				BufferedWriter bw = new BufferedWriter(new FileWriter(this.terms));
				bw.write("");
				bw.close();
			}
			catch(IOException e)
			{
				System.out.println("Error: " + e);
				e.printStackTrace();
			}
			//
			//
			//
			if(this.dison)
				System.out.println("\n\tExtracting terms with cutext... ");
			extractText();
			this.cutextData = createDataParameters();
			this.cutextArgs = createArgsCutext();
			cutext();
			if(this.dison)
				System.out.println("\n\tTerms extracted.");
		}
		
		//check listTerms
		if(listTerms.isEmpty())
		{
			System.err.println("The input file " + this.terms + " is empty. Nothing to do.");
			System.exit(1);
		}
		
		if(this.dison)
			System.out.println("\n\tDetermining negation of terms ...");
		genFileModifier();
		modifier();
		onlyNegTerms();
		if(this.dison)
			System.out.println("\n\tNegation of terms completed.");
    }
	
	public void extractText()
	{
		for(int i = 0; i < this.listText.size(); i++)
		{
			String line = this.listText.get(i);
			String parts[] = line.split("\\|");
			cutext.util.StringToFile.stringToFileAppend(parts[1] + "\n", this.temporary + this.inputCutext);
		}
	}
	
	
	
		
	/*****************************
		arguments for components
	*****************************/

	//
	public cutext.prepro.DatosEntrada createDataParameters()
	{
		cutext.prepro.DatosEntrada datos = new cutext.prepro.DatosEntrada();

		datos.fileLangSpa = this.routeconfigfilescutext + "tags-spa.txt";
		datos.fileLangEng = this.routeconfigfilescutext + "tags-eng.txt";
		datos.fileLangGal = this.routeconfigfilescutext + "tags-gal.txt";
		datos.fileLangCat = this.routeconfigfilescutext + "tags-cat.txt";

		datos.filePunctuation = this.routeconfigfilescutext + "punctuation.txt";
		datos.fileFrontiersPunctuation = this.routeconfigfilescutext + "frontiers-punctuation.txt";

		datos.fileSWspa = this.routeconfigfilescutext + "stop-words-spa.txt";
		datos.fileSWeng = this.routeconfigfilescutext + "stop-words-eng.txt";
		datos.fileSWgal = this.routeconfigfilescutext + "stop-words-gal.txt";
		datos.fileSWcat = this.routeconfigfilescutext + "stop-words-cat.txt";

		datos.setDirectorioEntrada(this.routeinternttcutext + "in");
		datos.setDirectorioIntermedio(this.routeinternttcutext + "x");
		datos.setDirectorioSalida(this.routeinternttcutext + "out");
		
		datos.setRouteHashTerms(this.routeHashTermsCutext);
		datos.setRouteTextFileHashTerms(this.routeTextFileHashTermsCutext);

		return datos;
	}

	public String[] createArgsCutext()
	{
		Hashtable<String,String> specialCases = new Hashtable<String,String>();
		specialCases.put("-language", this.language);
		specialCases.put("-inputFile", this.temporary + this.inputCutext);
		
		Properties p = new Properties();
		try
		{
			p.load(new FileReader(this.cutextProperties));
		}
		catch(FileNotFoundException e)
		{
			e.printStackTrace();
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
		
		return loadProperties(p, specialCases);
	}
	
	//
	public String[] createArgsModifier()
	{
		Hashtable<String,String> specialCases = new Hashtable<String,String>();
		
		specialCases.put("-language", this.language);
		specialCases.put("-routeInTextFile", this.temporary + this.inputModifier);
		//specialCases.put("-routeOutTextFile", this.outputFolder + this.outputFile);
		specialCases.put("-routeOutTextFile", this.temporary + this.outModifier);
		
		Properties p = new Properties();
		try
		{
			p.load(new FileReader(this.modifierProperties));
		}
		catch(FileNotFoundException e)
		{
			e.printStackTrace();
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
		
		return loadProperties(p, specialCases);
	}
	
	
	
	
	    
	public void cutext()
	{
		cutext.textmode.TextMode textmode = 
					new cutext.textmode.TextMode(this.cutextArgs, this.cutextData);
		//generar los términos en el archivo de entrada
		modOutCutext();
	}
	
	public void modOutCutext()
	{
		String fileName = "hsimpli.txt";
		List<String> listTermsCutext = 
			cutext.util.FileToString.asStringList(this.routeTextFileHashTermsCutext + fileName, StandardCharsets.UTF_8);
		for(int i = 0; i < listTermsCutext.size(); i++)
		{
			String line = listTermsCutext.get(i);
			String parts[] = line.split("\\|");
			String term = parts[0];
			cutext.util.StringToFile.stringToFileAppend(term + "\n", this.terms);
		}
		//obtained list of terms from file
		this.listTerms = cutext.util.FileToString.asStringList(this.terms, StandardCharsets.UTF_8);
	}
	
	//=======================================================
	//this.listTerms := term\nterm\nterm...
	//this.listText := id|sentence\nid|sentence\nid|sentence...
	//fileModifier := id\tterm\tsentence\nid\tterm\tsentence\nid\tterm\tsentence...
	//=======================================================
	public void genFileModifier()
	{
		for(int i = 0; i < this.listText.size(); i++)
		{
			String line = listText.get(i);
			String parts[] = line.split("\\|");
			String id = parts[0];
			String sentence = parts[1];
			//
			String auxSentence = getTermsForSentence(id, sentence);
			if(auxSentence.equals(""))
				continue;
			//add to modifier file
			cutext.util.StringToFile.stringToFileAppend(auxSentence + "\n", this.temporary + this.inputModifier);
		}
	}
	
	public String getTermsForSentence(String id, String sentence)
	{
		String out = "";
		for(int i = 0; i < this.listTerms.size(); i++)
		{
			String t = this.listTerms.get(i);
			if(contains(sentence, t))
			{
				out += id + "\t" + t + "\t\"" + sentence + "\"\n";
			}
		}
		return out.trim();
	}
	
	public boolean contains(String literal, String term)
	{
		//The following statement contains an error: consider the term as part of a word. For example: "la" is in paLAbra
		//return literal.contains(term);
		
		//use pattern regex with delimiter \b
		//escape and add full word limits - case-insensitive
		Pattern regex = Pattern.compile("\\b" + Pattern.quote(term) + "\\b", Pattern.CASE_INSENSITIVE);
		Matcher match = regex.matcher(literal);

		if(match.find())
			return true;
		return false;
	}
	
	public void modifier()
	{
		//call to modifier
		String modArgs[] = createArgsModifier();
		smn.main.Main modifier = new smn.main.Main(modArgs);
	}
	
	//Remain only with the negated terms
	public void onlyNegTerms()
	{
		List<String> listModifier = cutext.util.FileToString.asStringList(this.temporary + this.outModifier, StandardCharsets.ISO_8859_1);
		for(int i = 0; i < listModifier.size(); i++)
		{
			String line = listModifier.get(i);
			String parts[] = line.split("\\t");
			String state = parts[3];
			if(state.equals(smn.util.Statics.NEG))
			{
				//add to output file
				cutext.util.StringToFile.stringToFileAppend(line + "\n", this.outputFolder + this.outputFile);
			}
		}
	}
	
	
	
	
    
	/*****************************
		properties
	*****************************/
	
	public String[] loadProperties(Properties p, Hashtable<String,String> specialCases)
	{
		Enumeration<Object> keys = p.keys();
		List<String> largs = new ArrayList<String>();
		int i = 0;
		
		while (keys.hasMoreElements())
		{
			String key = (String)keys.nextElement();
			String value = (String)p.get(key);

			if((key.equals("-TM")) && (value.equals("NULL")))
			{
				largs.add(0, "-TM");
			}
			else if(specialCases.get(key) != null)
			{
				largs.add(key);
				value = specialCases.get(key);
				value = value.replace("/", Estaticos.FILE_SEP);
				largs.add(value);
			}
			else
			{
				largs.add(key);
				value = value.replace("/", Estaticos.FILE_SEP);
				largs.add(value);
			}
		}
		return largs.toArray(new String[0]);
	}
	
	
	
    
    
    
    
	//This will print the usage requirements and exit.
	private static void usage()
	{
		String message = "Usage: java scriptSequentialCutext.main.Main [options]\n"
				+ "\nOptions:\n"
				+ "  -help		: Show this message\n"
				//
				+ "  -displayon	<boolean>	: Show the messages at the standard output. Default TRUE (show)\n"
                //
                + "  -language	<string>			: SPANISH or ENGLISH. Default SPANISH\n"
				+ "  -execCutext	<boolean>			: Extract terms with cutext (true) or not (false). Default TRUE\n"
				//
				+ "  -text	<string>			: Input file with this format: id|sentence. Default at ../in/text.txt\n"
				+ "  -terms	<string>			: Name of the input file with terms or empty (depends on parameter execCutext is false or true). Default: at ../in/terms.txt\n"
				+ "  -temporary	<string>			: Temporary folder. Default at ../temp/\n"
				//
				+ "  -outputFolder	<string>			: Name of the output folder. Default: at ../out/\n"
				+ "  -outputFile	<string>			: Name of the output file. Default: \'negTerms.txt\'";
				
		System.err.println(message);
		System.exit(1);
	}
}





































