//package br.com.dbc.javamosdecolar.repository;
//
//import br.com.dbc.javamosdecolar.exception.DatabaseException;
//import br.com.dbc.javamosdecolar.model.CompanhiaEntity;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Repository;
//
//import java.sql.*;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Optional;
//@Repository
//@RequiredArgsConstructor
//public class CompanhiaRepositoryOld {
//
//    private final ConexaoBancoDeDados conexaoBancoDeDados;
//    public Integer getProximoId(Connection connection) throws SQLException {
//        try {
//            String sql = "SELECT seq_companhia.nextval mysequence from DUAL";
//            Statement statement = connection.createStatement();
//            ResultSet resultSet = statement.executeQuery(sql);
//
//            if (resultSet.next()) {
//                return resultSet.getInt("mysequence");
//            }
//            return null;
//        } catch (SQLException e) {
//            throw new DatabaseException(e.getCause());
//        }
//    }
//
//    public CompanhiaEntity create(CompanhiaEntity companhiaEntity) throws DatabaseException {
//        Connection conexao = null;
//
//        try {
//            conexao = conexaoBancoDeDados.getConnection();
//
//            Integer proximoId = this.getProximoId(conexao);
//            companhiaEntity.setIdCompanhia(proximoId);
//
//            String sql = "INSERT INTO COMPANHIA \n" +
//                    "(ID_COMPANHIA, CNPJ, NOME_FANTASIA, ID_USUARIO)\n" +
//                    "VALUES(?, ?, LOWER(?), ?)";
//
//            PreparedStatement preparedStatement = conexao.prepareStatement(sql);
//
//            preparedStatement.setInt(1, companhiaEntity.getIdCompanhia());
//            preparedStatement.setString(2, companhiaEntity.getCnpj());
//            preparedStatement.setString(3, companhiaEntity.getNomeFantasia());
//            preparedStatement.setInt(4, companhiaEntity.getIdUsuario());
//
//            preparedStatement.executeUpdate();
//            return companhiaEntity;
//
//        } catch (SQLException e) {
//            throw new DatabaseException(e.getCause());
//        } finally {
//            try{
//                if (conexao != null) {
//                    conexao.close();
//                }
//            } catch (SQLException e) {
//                e.printStackTrace();
//            }
//        }
//    }
//
//    public boolean update(Integer idCompanhia, CompanhiaEntity companhiaEntity) throws DatabaseException {
//        Connection conexao = null;
//        try {
//            conexao = conexaoBancoDeDados.getConnection();
//
//            StringBuilder sql = new StringBuilder();
//
//            sql.append("UPDATE COMPANHIA SET ");
//            sql.append("nome_fantasia = ? ");
//            sql.append("WHERE ID_COMPANHIA = ?");
//
//            PreparedStatement statement = conexao.prepareStatement(sql.toString());
//
//            statement.setString(1, companhiaEntity.getNomeFantasia());
//            statement.setInt(2, idCompanhia);
//
//            int result = statement.executeUpdate();
//
//            return result > 0;
//
//        } catch (SQLException e) {
//            throw new DatabaseException(e.getCause());
//        } finally {
//            try{
//                if (conexao != null) {
//                    conexao.close();
//                }
//            } catch (SQLException e) {
//                e.printStackTrace();
//            }
//        }
//    }
//
//    public List<CompanhiaEntity> getAll() throws DatabaseException {
//        List<CompanhiaEntity> companhiaEntities = new ArrayList<>();
//        Connection conexao = null;
//
//        try {
//            conexao = conexaoBancoDeDados.getConnection();
//            Statement statement = conexao.createStatement();
//
//            String sql = "SELECT c.ID_COMPANHIA, c.CNPJ, c.NOME_FANTASIA, c.ID_USUARIO, u.ATIVO, " +
//                    "u.LOGIN, u.SENHA, u.NOME \n" +
//                    "FROM COMPANHIA c \n" +
//                    "INNER JOIN USUARIO u \n" +
//                    "ON c.ID_USUARIO = u.ID_USUARIO";
//
//            ResultSet resultSet = statement.executeQuery(sql);
//
//            while (resultSet.next()) {
//                CompanhiaEntity companhiaEntity = getByResultSet(resultSet);
//                companhiaEntities.add(companhiaEntity);
//            }
//
//            return companhiaEntities;
//        } catch (SQLException e) {
//            e.printStackTrace();
//            throw new DatabaseException(e.getCause());
//
//        } finally {
//            try{
//                if (conexao != null) {
//                    conexao.close();
//                }
//            } catch (SQLException e) {
//                e.printStackTrace();
//            }
//        }
//    }
//
//    public Optional<CompanhiaEntity> getByNome(String nome) throws DatabaseException {
//        Connection conexao = null;
//        try{
//            conexao = conexaoBancoDeDados.getConnection();
//
//            String sql = "SELECT c.ID_COMPANHIA, c.CNPJ, c.NOME_FANTASIA, c.ID_USUARIO, u.LOGIN, u.ATIVO," +
//                    " u.SENHA, u.NOME \n" +
//                    "FROM COMPANHIA c \n" +
//                    "INNER JOIN USUARIO u \n" +
//                    "ON c.ID_USUARIO = u.ID_USUARIO \n" +
//                    "WHERE c.NOME_FANTASIA LIKE ?";
//
//            PreparedStatement statement = conexao.prepareStatement(sql);
//
//            statement.setString(1, "%" + nome + "%".toLowerCase());
//
//            ResultSet resultSet = statement.executeQuery();
//
//            if(resultSet.next()) {
//                CompanhiaEntity companhiaEntityPesquisa = getByResultSet(resultSet);
//                return Optional.of(companhiaEntityPesquisa);
//            } else {
//                return Optional.empty();
//            }
//
//        } catch (SQLException e) {
//            e.printStackTrace();
//            throw new DatabaseException(e.getCause());
//        } finally {
//            try {
//                if (conexao != null) {
//                    conexao.close();
//                }
//            } catch (SQLException e) {
//                e.printStackTrace();
//            }
//        }
//    }
//
//    public Optional<CompanhiaEntity> getById(Integer id) throws DatabaseException {
//        Connection conexao = null;
//        try{
//            conexao = conexaoBancoDeDados.getConnection();
//
//            String sql = "SELECT c.ID_COMPANHIA, c.CNPJ, c.NOME_FANTASIA, c.ID_USUARIO, u.LOGIN, u.SENHA, u.NOME, u.ATIVO\n" +
//                    "FROM COMPANHIA c \n" +
//                    "INNER JOIN USUARIO u \n" +
//                    "ON c.ID_USUARIO = u.ID_USUARIO " +
//                    "WHERE c.ID_COMPANHIA = ?";
//
//            PreparedStatement statement = conexao.prepareStatement(sql);
//
//            statement.setInt(1, id);
//
//            ResultSet resultSet = statement.executeQuery();
//
//            if(resultSet.next()) {
//                CompanhiaEntity companhiaEntityPesquisa = getByResultSet(resultSet);
//                return Optional.of(companhiaEntityPesquisa);
//            } else {
//                return Optional.empty();
//            }
//
//        } catch (SQLException e) {
//            throw new DatabaseException(e.getCause());
//        } finally {
//            try {
//                if (conexao != null) {
//                    conexao.close();
//                }
//            } catch (SQLException e) {
//                e.printStackTrace();
//            }
//        }
//    }
//
//    private CompanhiaEntity getByResultSet(ResultSet resultSet) throws SQLException {
//        CompanhiaEntity companhiaEntity = new CompanhiaEntity();
//
//        companhiaEntity.setIdCompanhia(resultSet.getInt("id_companhia"));
//        companhiaEntity.setCnpj(resultSet.getString("cnpj"));
//        companhiaEntity.setNomeFantasia(resultSet.getString("nome_fantasia"));
//        companhiaEntity.setIdUsuario(resultSet.getInt("id_usuario"));
//        companhiaEntity.setLogin(resultSet.getString("login"));
//        companhiaEntity.setSenha(resultSet.getString("senha"));
//        companhiaEntity.setNome(resultSet.getString("nome"));
//
//        int ativo = resultSet.getInt("ativo");
//
//        if(ativo == 1) {
//            companhiaEntity.setAtivo(true);
//        } else {
//            companhiaEntity.setAtivo(false);
//        }
//
//        return companhiaEntity;
//    }
//}