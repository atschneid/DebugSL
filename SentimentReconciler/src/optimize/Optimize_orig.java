package optimize;

import gurobi.*;
import data.*;
import misc.*;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import org.apache.commons.lang3.StringUtils;

public class Optimize_orig
{
	private static final double EPSILON = 1e-3;
       	private static final int MAXSTRINGSIZE = 100;

	
	private static class ConstraintArrays
	{
		GRBLinExpr[] linexps;
		char[] senses;
		double[] rhs;
		String[] names;
		GRBModel model;
                
		public ConstraintArrays(Set<Word> words, GRBModel model)
		{
			int size = 2*words.size();
			this.model = model;
                        
                        GRBLinExpr[] linexpsWorking = new GRBLinExpr[size];
			char[] sensesWorking = new char[size] ;
			double[] rhsWorking = new double[size];
			String[] namesWorking = new String[size];
                        
			String polarity = null;
			float divisor;
			Map<Synset, Integer> map;
                        
                        double freq;
                        GRBVar var;
			
                        int n = 0;
                        int countNeut = 0;
			for(Word word : words)
			{
				divisor = (float) word.getFrequencySum();
				
                                switch(word.getPolarity())
				{
                                    case POSITIVE: polarity = "pos"; break;
                                    case NEGATIVE: polarity = "neg"; break;
                                    default: polarity = null; break;
				}
				
				map = word.getFrequenciesBySynsetRelateTo();
				
				if(polarity != null && (polarity.equals("pos") || polarity.equals("neg")))
				{
                                    linexpsWorking[n] = new GRBLinExpr();
                                    
                                    for(Synset synset : map.keySet())
					{
                                            try
                                            {
                                                freq = ((double) map.get(synset)) / divisor;
                                                var = model.getVarByName(StringUtils.abbreviate(synset.getGloss(), MAXSTRINGSIZE) + ":" + polarity);
                                                
						linexpsWorking[n].addTerm(freq, var);
                                            }
                                            catch(GRBException e)
                                            {
                                                throw new GeneralException(e);
                                            }
                                        }
					sensesWorking[n] = GRB.GREATER_EQUAL;
					rhsWorking[n] = 0.5 + EPSILON;
                                        namesWorking[n] = word.getWord();                                
				} else {
					
                                    linexpsWorking[n] = new GRBLinExpr();
                                    linexpsWorking[size/2 + countNeut] = new GRBLinExpr();
                                    
                                    for(Synset synset : map.keySet())
					{
                                            try
                                            {
						linexpsWorking[n].addTerm(((double)(map.get(synset))/divisor), model.getVarByName(StringUtils.abbreviate(synset.getGloss(), MAXSTRINGSIZE) + ":" + "pos"));
						linexpsWorking[size/2 + countNeut].addTerm(((double)(map.get(synset))/divisor), model.getVarByName(StringUtils.abbreviate(synset.getGloss(), MAXSTRINGSIZE) + ":" + "neg"));
                                            }
                                            catch(GRBException e)
                                            {
                                                throw new GeneralException(e);
                                            }
					}
					sensesWorking[n] = GRB.LESS_EQUAL;
                                        sensesWorking[size/2 + countNeut] = GRB.LESS_EQUAL;
					rhsWorking[n] = 0.5;
                                        rhsWorking[size/2 + countNeut] = 0.5;
                                        namesWorking[n] = word.getWord() + ":Neu+";
            				namesWorking[size/2 + countNeut] = word.getWord() + ":Neu-";
                                        countNeut++;
				}
				n++;
			}
                        linexps = new GRBLinExpr[size/2 + countNeut];
			senses = new char[size/2 + countNeut] ;
			rhs = new double[size/2 + countNeut];
			names = new String[size/2 + countNeut];
                        
                        linexps = Arrays.copyOfRange(linexpsWorking, 0, countNeut + size/2);
                        senses = Arrays.copyOfRange(sensesWorking, 0, countNeut + size/2);
                        rhs = Arrays.copyOfRange(rhsWorking, 0, countNeut + size/2);
                        names = Arrays.copyOfRange(namesWorking, 0, countNeut + size/2);
		}
		public ConstraintArrays(Set<Synset> synsets, GRBVar[] vars, GRBModel model)
		{
			int size = synsets.size();
			this.model = model;
                        
			linexps = new GRBLinExpr[size];
			senses = new char[size] ;
			rhs = new double[size];
			names = new String[size];
			
                        for(int i=0;i<size;i++)
                        {
                            linexps[i] = new GRBLinExpr();
                        }
                        /*
                        System.out.println(size);
                        try{
                            System.out.println(vars[0].get(GRB.StringAttr.VarName));
                        }
                        catch(GRBException e)
                        {
                            throw new GeneralException(e);
                        }
                        */
			for(int n=0;n<size;n++){
				linexps[n].addTerm(1, vars[2*n]);
				linexps[n].addTerm(1, vars[2*n + 1]);
				
				senses[n] = GRB.LESS_EQUAL;
				rhs[n] = 1;
                                try{
                                           names[n] = "Synset Constraint: " + StringUtils.abbreviate(vars[2*n].get(GRB.StringAttr.VarName), 20);
                                }
                                catch(GRBException e)
                                {
                                    throw new GeneralException(e);
                                }
                         
			}
		}
                
                public void addConstraintArray()
                {
                    
                    try
                    {
                    model.addConstrs(linexps, senses, rhs, names);
                    }
                    catch(GRBException e)
                    {
                        throw new GeneralException(e);
                    }
                }
	}
	
	public static ArrayList<Component> solve(List<Component> components)
	{
            ArrayList<GRBModel> infeasibles = new ArrayList();
            ArrayList<Component> badComps = new ArrayList();
            GRBModel model;
            for(int componentCount = 1; componentCount <= components.size(); componentCount++)
            {
                //System.out.println("Component # : " + componentCount);
                model = Optimize_orig.solveFeasible(components.get(componentCount - 1));
                if(model != null)
                    infeasibles.add(model);
                    badComps.add(components.get(componentCount - 1));
            }
            return badComps;
            //System.out.println("\n\n*****IIS******\n\n");
//            System.out.println(infeasibles.size() + " Infeasible System(s):");
//            for(int modelCount = 1; modelCount <= infeasibles.size(); modelCount++)
//            {
//                System.out.println("===================");
//                System.out.print("System : " + modelCount + ", ");
//                
//                Optimize.solveInFeasible(infeasibles.get(modelCount - 1));
//            }
//            
        }
        
        private static GRBModel solveFeasible(Component component)
        {
            GRBModel model = null;
            ConstraintArrays constraintArrays;
            
            //System.out.println("*********************NEW*COMPONENT***************************");
            
            //System.out.println("#SYNSETS:" + component.getAllSynsets().size()+":#WORDS:" + component.getAllWords().size());
            
            for(Word word : component.getAllWords())
            {
                //System.out.print(word.getWord() + ", ");
            }
            
            //System.out.println();
            
            GRBEnv env;
            try
            {
                env = new GRBEnv();
		model = new GRBModel(env);
            }
            catch(GRBException e)
                    {
                        System.out.println(e.getMessage());
                        throw new GeneralException(e);
                    }
		String[] namesArray = new String[2 * component.getSynsetsNumber()];
                double[] lb = new double[2 * component.getSynsetsNumber()];
		int i = 0;
		
		for(Synset synset : component.getAllSynsets())
		{
                    namesArray[i] = StringUtils.abbreviate(synset.getGloss(), MAXSTRINGSIZE) + ":" + "pos";
                    namesArray[i+1] = StringUtils.abbreviate(synset.getGloss(), MAXSTRINGSIZE) + ":" + "neg";
                    lb[i] = 0; lb[i+1] = 0;
                    i += 2;
		}
		GRBVar[] synsetVarsArray = null;
		try
                {
                    synsetVarsArray = model.addVars(lb, null, null, null, namesArray);
                    model.update();
		}
                catch(GRBException e) 
                {
                    throw new GeneralException(e);
                }
                try
                {
                    constraintArrays = new ConstraintArrays(component.getAllWords(), model);
                    constraintArrays.addConstraintArray();
                    
                    constraintArrays = new ConstraintArrays(component.getAllSynsets(), synsetVarsArray, model);
                    constraintArrays.addConstraintArray();
                }
                catch(Exception e) 
                {
                    throw new GeneralException(e);
                }    
                try
                {
                    model.update();
                }
                
                catch(GRBException e) 
                {
                    throw new GeneralException(e);
                }
                
                int optimstatus;
                
                try
                {
                    model.getEnv().set(GRB.IntParam.Presolve, 0);
                    model.getEnv().set(GRB.IntParam.OutputFlag, 0);
                    model.optimize();
                    optimstatus = model.get(GRB.IntAttr.Status);

                    if (optimstatus == GRB.Status.INFEASIBLE) {
                        //System.out.println("System is infeasible");
                        return model;
                        
                    } else {
                        for(GRBVar variable : synsetVarsArray)
                        {
                            //System.out.println(variable.get(GRB.StringAttr.VarName) + " : " + variable.get(GRB.DoubleAttr.X));
                        }
                    }
                }
                catch(GRBException e)
                {
                    throw new GeneralException(e);
                }
		
                
                //System.out.println();
                try
                {
                    env.dispose();
                }
                catch(GRBException e)
                {
                    throw new GeneralException(e);
                }
                return null;
	}
        
        private static void solveInFeasible(GRBModel model)
        {
            boolean first;
            String name = null;
            int countWords = 0; 
            int countSynsets = 0;
            //System.out.println();
            //System.out.println("=====================");
            try
            {
                for(GRBConstr constraint : model.getConstrs()) 
                {
                    if(!constraint.get(GRB.StringAttr.ConstrName).startsWith("Synset Constraint:"))
                    {
                        if(!constraint.get(GRB.StringAttr.ConstrName).endsWith("Neu-"))
                            countWords++;  
                    }else{
                        countSynsets++;
                    }
                }
                
                System.out.println("Number of words : " + countWords + ", Number of synsets : " + countSynsets);
                System.out.println("===================");
                for(GRBConstr constraint : model.getConstrs()) 
                {
                    if(!constraint.get(GRB.StringAttr.ConstrName).startsWith("Synset Constraint:"))
                    {
                        System.out.print(constraint.get(GRB.StringAttr.ConstrName) + " ");
                    }
                }
                System.out.println();
                System.out.println();
                int countRemoved = 0;
                while(model.get(GRB.IntAttr.Status) == GRB.Status.INFEASIBLE)
                {
                    model.computeIIS();
                    
                    System.out.println("IIS:");
                    
                    first = true;
                    for(GRBConstr constraint : model.getConstrs()) {
                        if(constraint.get(GRB.IntAttr.IISConstr) == 1)
                        {
                            if(!constraint.get(GRB.StringAttr.ConstrName).startsWith("Synset Constraint:"))
                                System.out.print(constraint.get(GRB.StringAttr.ConstrName) + ", ");
                            if(first)
                            {
                                first = false;
                                name = constraint.get(GRB.StringAttr.ConstrName);
                                model.remove(constraint);
                                countRemoved++;
                            }
                        }
                    }
                    System.out.println();
                    System.out.println("Removing word \"" + name + "\"");
                    System.out.println();
                    model.update();
                    model.optimize();
                }
                System.out.println("System is FEASIBLE after " + countRemoved + " word(s) removed.\n");
                for(GRBVar variable : model.getVars())
                    {
                        //System.out.println(variable.get(GRB.StringAttr.VarName) + " : " + variable.get(GRB.DoubleAttr.X));
                    }
            }
            catch(GRBException e)
            {
                throw new GeneralException(e);
            }
            

        }
}