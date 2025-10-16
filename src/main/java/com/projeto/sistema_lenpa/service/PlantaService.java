package com.projeto.sistema_lenpa.service;

import com.projeto.sistema_lenpa.model.planta.Planta;
import com.projeto.sistema_lenpa.model.planta.PlantaDTO;
import com.projeto.sistema_lenpa.model.planta.PlantaListaDTO;
import com.projeto.sistema_lenpa.repository.EntregaRepository;
import com.projeto.sistema_lenpa.repository.LoteMudasRepository;
import com.projeto.sistema_lenpa.repository.PlantaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class PlantaService {

    @Autowired
    private PlantaRepository plantaRepository;

    @Autowired
    private EntregaRepository entregaRepository;

    @Autowired
    private LoteMudasRepository loteMudasRepository;

    @Value("${upload.path.images}")
    private String uploadPath;

    public List<PlantaListaDTO> getPlantas() {

        List<Planta> plantas = plantaRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
        List<PlantaListaDTO> dtosParaRetornar = new ArrayList<>();

        for (Planta planta : plantas) {
            // Verifica se a planta está em uso em entregas OU em lotes
            boolean temVinculoEntrega = entregaRepository.existsByPlantaId(planta.getId());
            boolean temVinculoLote = loteMudasRepository.existsByPlantaId(planta.getId());
            boolean temVinculos = temVinculoEntrega || temVinculoLote;

            dtosParaRetornar.add(new PlantaListaDTO(planta, !temVinculos));
        }

        return dtosParaRetornar;
    }

    @Transactional
    public Planta cadastrarPlanta(PlantaDTO plantaDTO, MultipartFile imagem) throws IOException {
        Planta planta = plantaDTO.toEntity();

        // Chama método para salvar a imagem e obtém o nome do arquivo
        String nomeDaImagemSalva = salvarImagem(imagem);

        // Define o nome do arquivo na entidade antes de salvar no banco
        planta.setFoto_url(nomeDaImagemSalva);

        plantaRepository.save(planta);
        return planta;
    }

    public PlantaDTO buscaPlantaEdicao(int id) {

        Planta planta = plantaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Planta com ID " + id + " não encontrada."));

        return PlantaDTO.fromEntity(planta);
    }

    public void editarPlanta(int id, PlantaDTO plantaDTO, MultipartFile imagem) throws IOException{

        //Busca a planta existente no banco.
        Planta plantaExistente = plantaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Planta com ID " + id + " não encontrada."));

        //ATUALIZA OS CAMPOS DE TEXTO PRIMEIRO, usando os dados do DTO.
        //Garanta que PlantaDTO tenha um método 'updateEntity' para isso.
        plantaDTO.updateEntity(plantaExistente);

        //DEPOIS DE ATUALIZAR O TEXTO, lida com a lógica da imagem.
        //Verifica se uma nova imagem foi de fato enviada.
        if (imagem != null && !imagem.isEmpty()) {

            //Deleta a imagem antiga para não deixar lixo no servidor.
            deletarImagemAntiga(plantaExistente.getFoto_url());

            //Salva a nova imagem e obtém o nome do novo arquivo.
            String nomeNovaImagem = salvarImagem(imagem);

            //Define o novo nome do arquivo na entidade. Esta será a última modificação no campo 'foto'.
            plantaExistente.setFoto_url(nomeNovaImagem);
        }

        //Salva a entidade no banco com todas as alterações aplicadas na ordem correta.
        plantaRepository.save(plantaExistente);

    }

    public void deletarPlanta(int id) {
        Planta planta = plantaRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Falha ao deletar: Planta com ID " + id + " não encontrado."));
        plantaRepository.delete(planta);
    }

    public List<Planta> buscarPlantasPublico(String termoBusca, String tipoPlanta) {
        return plantaRepository.search(termoBusca, tipoPlanta);
    }

    // Método privado para salvar o arquivo de imagem
    private String salvarImagem(MultipartFile imagem) throws IOException {

        if (imagem == null || imagem.isEmpty()) {
            return null;
        }

        // Gera um nome de arquivo único para evitar que arquivos com o mesmo nome se sobrescrevam
        String nomeOriginal = StringUtils.cleanPath(imagem.getOriginalFilename());
        String extensao = nomeOriginal.substring(nomeOriginal.lastIndexOf("."));
        String nomeUnico = UUID.randomUUID().toString() + extensao;

        Path caminhoCompleto = Paths.get(uploadPath, nomeUnico);

        // Cria os diretórios necessários (caso não existam)
        Files.createDirectories(caminhoCompleto.getParent());

        Files.copy(imagem.getInputStream(), caminhoCompleto, StandardCopyOption.REPLACE_EXISTING);

        return nomeUnico;
    }

    // Método auxiliar para deletar a imagem antiga
    private void deletarImagemAntiga(String nomeImagem) {
        if (nomeImagem == null || nomeImagem.isEmpty()) {
            return;
        }
        try {
            Path caminhoArquivo = Paths.get(uploadPath, nomeImagem);
            Files.deleteIfExists(caminhoArquivo);
        } catch (IOException e) {
            System.err.println("Falha ao deletar imagem antiga: " + nomeImagem);
            e.printStackTrace();
        }
    }
}
