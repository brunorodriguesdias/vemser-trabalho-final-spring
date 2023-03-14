//package br.com.dbc.javamosdecolar.repository;
//
//import br.com.dbc.javamosdecolar.exception.DatabaseException;
//import br.com.dbc.javamosdecolar.model.TipoUsuario;
//import br.com.dbc.javamosdecolar.model.UsuarioEntity;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Repository;
//
//import java.sql.*;
//import java.util.Optional;
//@Repository
//@RequiredArgsConstructor
//public class UsuarioRepositoryOld {
//
//    private final ConexaoBancoDeDados conexaoBancoDeDados;
//
//    public Integer getProximoId(Connection connection) throws SQLException {
//        try {
//            String sql = "SELECT seq_usuario.nextval mysequence from DUAL";
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
//    public UsuarioEntity create(UsuarioEntity usuarioEntity) throws DatabaseException {
//        Connection conexao = null;
//        try {
//            conexao = conexaoBancoDeDados.getConnection();
//
//            Integer proximoId = this.getProximoId(conexao);
//            usuarioEntity.setIdUsuario(proximoId);
//
//            String sql = "INSERT INTO USUARIO \n" +
//                    "(ID_USUARIO, LOGIN, SENHA, NOME, TIPO, ATIVO)\n" +
//                    "VALUES(?, LOWER(?), ?, LOWER(?), ?, ?)";
//
//            PreparedStatement preparedStatement = conexao.prepareStatement(sql);
//
//            preparedStatement.setInt(1, usuarioEntity.getIdUsuario());
//            preparedStatement.setString(2, usuarioEntity.getLogin());
//            preparedStatement.setString(3, usuarioEntity.getSenha());
//            preparedStatement.setString(4, usuarioEntity.getNome());
//            preparedStatement.setInt(5, usuarioEntity.getTipoUsuario().getTipo());
//            preparedStatement.setInt(6, 1);
//
//            preparedStatement.executeUpdate();
//            return usuarioEntity;
//
//        } catch (SQLException e) {
//            e.printStackTrace();
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
//    public boolean update(Integer idUsuario, UsuarioEntity usuarioEntity) throws DatabaseException {
//        Connection conexao = null;
//        try{
//            conexao = conexaoBancoDeDados.getConnection();
//
//            StringBuilder sql = new StringBuilder();
//            sql.append("UPDATE USUARIO SET ");
//            sql.append("senha = ?, ");
//            sql.append("nome = ? ");
//            sql.append("WHERE id_usuario = ?");
//
//            PreparedStatement statement = conexao.prepareStatement(sql.toString());
//
//            statement.setString(1, usuarioEntity.getSenha());
//            statement.setString(2, usuarioEntity.getNome());
//            statement.setInt(3, idUsuario);
//
//            int res = statement.executeUpdate();
//            return res  > 0;
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
//    public boolean delete(Integer id) throws DatabaseException {
//        Connection conexao = null;
//        try{
//            conexao = conexaoBancoDeDados.getConnection();
//
//            StringBuilder sql = new StringBuilder();
//            sql.append("UPDATE USUARIO SET ");
//            sql.append("ativo = 0");
//            sql.append("WHERE id_usuario = ?");
//
//            PreparedStatement statement = conexao.prepareStatement(sql.toString());
//            statement.setInt(1, id);
//
//            int res = statement.executeUpdate();
//            return res  > 0;
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
//    public Optional<UsuarioEntity> getByLogin(String login) throws DatabaseException {
//        UsuarioEntity usuarioPesquisa = new UsuarioEntity();
//        Connection conexao = null;
//        try{
//            conexao = conexaoBancoDeDados.getConnection();
//
//            String sql = "SELECT * FROM USUARIO WHERE login = LOWER(?)";
//            PreparedStatement statement = conexao.prepareStatement(sql);
//
//            statement.setString(1, login);
//
//            ResultSet resultSet = statement.executeQuery();
//
//            if(resultSet.next()) {
//                usuarioPesquisa.setIdUsuario(resultSet.getInt("id_usuario"));
//                usuarioPesquisa.setLogin(resultSet.getString("login"));
//                usuarioPesquisa.setSenha(resultSet.getString("senha"));
//                usuarioPesquisa.setNome(resultSet.getString("nome"));
//                String tipo = resultSet.getString("tipo");
//
//                if (tipo.equals("1")) {
//                    usuarioPesquisa.setTipoUsuario(TipoUsuario.COMPANHIA);
//
//                } else if (tipo.equals("2")) {
//                    usuarioPesquisa.setTipoUsuario(TipoUsuario.COMPRADOR);
//                }
//
//                int ativo = resultSet.getInt("ativo");
//                if (ativo == 1) {
//                    usuarioPesquisa.setAtivo(true);
//                } else {
//                    usuarioPesquisa.setAtivo(false);
//                }
//
//                return Optional.of(usuarioPesquisa);
//
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
//    public Optional<UsuarioEntity> getById(Integer idUsuario) throws DatabaseException {
//        UsuarioEntity usuarioPesquisa = new UsuarioEntity();
//        Connection conexao = null;
//        try{
//            conexao = conexaoBancoDeDados.getConnection();
//
//            String sql = "SELECT ID_USUARIO, LOGIN, SENHA, NOME, TIPO, ATIVO FROM USUARIO u \n" +
//                    "WHERE u.ID_USUARIO = ?";
//            PreparedStatement statement = conexao.prepareStatement(sql);
//
//            statement.setInt(1, idUsuario);
//
//            ResultSet resultSet = statement.executeQuery();
//
//            if(resultSet.next()) {
//                usuarioPesquisa.setIdUsuario(resultSet.getInt("id_usuario"));
//                usuarioPesquisa.setLogin(resultSet.getString("login"));
//                usuarioPesquisa.setSenha(resultSet.getString("senha"));
//                usuarioPesquisa.setNome(resultSet.getString("nome"));
//
//                int tipo = resultSet.getInt("tipo");
//                if (tipo == 1) {
//                    usuarioPesquisa.setTipoUsuario(TipoUsuario.COMPANHIA);
//                } else {
//                    usuarioPesquisa.setTipoUsuario(TipoUsuario.COMPRADOR);
//                }
//
//                int ativo = resultSet.getInt("ativo");
//                if (ativo == 1) {
//                    usuarioPesquisa.setAtivo(true);
//                } else {
//                    usuarioPesquisa.setAtivo(false);
//                }
//
//                return Optional.of(usuarioPesquisa);
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
//}
