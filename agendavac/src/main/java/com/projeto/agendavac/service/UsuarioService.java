    package com.projeto.agendavac.service;

    import com.projeto.agendavac.entity.Alergia;
    import com.projeto.agendavac.entity.Usuario;
    import com.projeto.agendavac.entity.UsuarioAlergia;
    import com.projeto.agendavac.repository.AlergiaRepository;
    import com.projeto.agendavac.repository.UsuarioRepository;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.stereotype.Service;

    import java.util.List;
    import java.util.NoSuchElementException;
    import java.util.stream.Collectors;

    @Service
    public class UsuarioService {

        @Autowired
        private UsuarioRepository usuarioRepository;

        @Autowired
        private AlergiaRepository alergiaRepository;

        public Usuario salvar(Usuario usuario) {

            if (usuario.getAlergias() != null) {
                List<Alergia> alergiasPersistidas = usuario.getAlergias().stream()
                        .map(alergia -> alergiaRepository.findById(alergia.getId())
                                .orElseThrow(() -> new NoSuchElementException("Alergia com ID " + alergia.getId() + " não encontrada.")))
                        .toList();
                usuario.setAlergias(alergiasPersistidas);
            } else {
                usuario.setAlergias(null);
            }

            return usuarioRepository.save(usuario);
        }

        public List<Usuario> listarTodos() {
            List<Usuario> usuarios = usuarioRepository.findAll();

            if (usuarios.isEmpty()) {
                throw new NoSuchElementException("Nenhum usuário cadastrado.");
            }

            return usuarios;
        }

        public Usuario buscarPorId(Long id) {

            return usuarioRepository.findById(id)
                    .orElseThrow(() -> new NoSuchElementException("Usuário com ID " + id + " não encontrado."));

        }

        public List<Usuario> buscarPorNome(String nome) {

            List<Usuario> usuarios = usuarioRepository.findByNomeContainingIgnoreCase(nome);

            if (usuarios.isEmpty()) {
                throw new NoSuchElementException("Nenhum usuário encontrado com o nome fornecido: " + nome);
            }

            return usuarios;
        }

        public Usuario atualizar(Long id, Usuario usuarioAtualizado) {
            Usuario usuario = usuarioRepository.findById(id)
                    .orElseThrow(() -> new NoSuchElementException("Usuário com ID " + id + " não encontrado."));

            // Atualiza os dados básicos do usuário
            usuario.setNome(usuarioAtualizado.getNome());
            usuario.setDataNascimento(usuarioAtualizado.getDataNascimento());
            usuario.setSexo(usuarioAtualizado.getSexo());
            usuario.setLogradouro(usuarioAtualizado.getLogradouro());
            usuario.setSetor(usuarioAtualizado.getSetor());
            usuario.setCidade(usuarioAtualizado.getCidade());
            usuario.setUf(usuarioAtualizado.getUf());

            // Atualiza as associações de alergias
            if (usuarioAtualizado.getAlergias() != null) {

                // Recupera as alergias do banco para persistência
                List<Alergia> alergiasPersistidas = usuarioAtualizado.getAlergias().stream()
                        .map(alergia -> alergiaRepository.findById(alergia.getId())
                                .orElseThrow(() -> new NoSuchElementException("Alergia com ID " + alergia.getId() + " não encontrada.")))
                        .collect(Collectors.toList());

                usuario.getAlergias().addAll(alergiasPersistidas);

                usuario.setAlergias(alergiasPersistidas);

            } else {
                // Caso tenha editado a alergia do usuário e removido todas
                usuario.getAlergias().clear();;
            }

            // Salva o usuário atualizado
            return usuarioRepository.save(usuario);
        }


        public void deletar(Long id) {

            if (!usuarioRepository.existsById(id)) {
                throw new NoSuchElementException("Usuário com ID " + id + " não encontrado.");
            }

            usuarioRepository.deleteById(id);
        }

    }

