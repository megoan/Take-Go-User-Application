package com.example.fotij.userapplication.model.backend;

/**
 * Created by fotij on 23/12/2017.
 */

public class FactoryMethod {
    private static BackEndFunc backEndFunc=null;
    private static BackEndFunc backEndFuncSQL=null;

    private FactoryMethod(DataSourceType dataSourceType) {

    }
    public static BackEndFunc getBackEndFunc(DataSourceType dataSourceType)
    {
        switch (dataSourceType){
            case DATA_INTERNET:
            {
                if(backEndFuncSQL==null)
                {
                    backEndFuncSQL=new BackEndForSql();
                }
                return backEndFuncSQL;
            }
            case DATA_LIST:
            {
                if(backEndFunc==null)
                {
                    backEndFunc=new BackEndForList();
                }
            }
        }
        return backEndFunc;
    }
}

