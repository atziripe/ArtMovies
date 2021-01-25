/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipn.mx.utilerias;

import java.sql.Connection;
import java.sql.SQLException;
import javax.sql.DataSource;
import org.apache.commons.dbcp2.BasicDataSource;

/**
 *
 * @author El TEAM AFR
 */
public class Conexion {
    private Connection con;
    BasicDataSource basicDataSource = new BasicDataSource();
    public Connection conecta() throws SQLException {
        basicDataSource.setDriverClassName("org.postgresql.Driver");
        basicDataSource.setUsername("pftbwhrhrclrkd");
        basicDataSource.setPassword("238b94901e8de48ba7a717b12da48e377c1286111ab4b995d02455f2a0cede6e");
        basicDataSource.setUrl("jdbc:postgresql://ec2-54-159-107-189.compute-1.amazonaws.com:5432/d8rldgntsq6b1m?sslmode=require");
        basicDataSource.setValidationQuery("select 1");
        con = null;
        try {
            DataSource dataSource = basicDataSource;
            con = dataSource.getConnection();
            System.out.println("Conexion establecida");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return con;
    }
}
