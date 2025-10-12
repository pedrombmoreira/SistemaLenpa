package com.projeto.sistema_lenpa.service;

import com.projeto.sistema_lenpa.model.planta.Planta;
import com.projeto.sistema_lenpa.model.planta.PlantaDTO;
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
import java.util.List;
import java.util.UUID;

@Service
public class PlantaService {

    @Autowired
    private PlantaRepository plantaRepository;

    @Value("${upload.path.images}")
    private String uploadPath;

    public List<Planta> getPlantas() {
        return plantaRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
    }

    @Transactional
    public Planta cadastrarPlanta(PlantaDTO plantaDTO, MultipartFile imagem) throws IOException {
        Planta planta = plantaDTO.toEntity(); // Supondo que o DTO construa o objeto

        // Chama nosso método para salvar a imagem e obtém o nome do arquivo
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

        // 1. Busca a planta existente no banco.
        Planta plantaExistente = plantaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Planta com ID " + id + " não encontrada."));

        // 2. ATUALIZA OS CAMPOS DE TEXTO PRIMEIRO, usando os dados do DTO.
        // Garanta que seu PlantaDTO tenha um método 'updateEntity' para isso.
        plantaDTO.updateEntity(plantaExistente);

        // 3. AGORA, DEPOIS DE ATUALIZAR O TEXTO, lida com a lógica da imagem.
        // Verifica se uma nova imagem foi de fato enviada.
        if (imagem != null && !imagem.isEmpty()) {

            // 3.1. Deleta a imagem antiga para não deixar lixo no servidor.
            deletarImagemAntiga(plantaExistente.getFoto_url());

            // 3.2. Salva a nova imagem e obtém o nome do novo arquivo.
            String nomeNovaImagem = salvarImagem(imagem);

            // 3.3. Define o novo nome do arquivo na entidade. Esta será a última modificação no campo 'foto'.
            plantaExistente.setFoto_url(nomeNovaImagem);
        }

        // 4. Salva a entidade no banco com todas as alterações aplicadas na ordem correta.
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
        // Se nenhuma imagem for enviada, não faz nada
        if (imagem == null || imagem.isEmpty()) {
            return null;
        }

        // Gera um nome de arquivo único para evitar que arquivos com o mesmo nome se sobrescrevam
        String nomeOriginal = StringUtils.cleanPath(imagem.getOriginalFilename());
        String extensao = nomeOriginal.substring(nomeOriginal.lastIndexOf("."));
        String nomeUnico = UUID.randomUUID().toString() + extensao;

        // Constrói o caminho completo: /sua/pasta/de/uploads/nome-unico.jpg
        Path caminhoCompleto = Paths.get(uploadPath, nomeUnico);

        // Cria os diretórios necessários (caso não existam)
        Files.createDirectories(caminhoCompleto.getParent());

        // Copia o arquivo enviado para o destino final
        Files.copy(imagem.getInputStream(), caminhoCompleto, StandardCopyOption.REPLACE_EXISTING);

        // Retorna apenas o nome do arquivo para ser salvo no banco
        return nomeUnico;
    }

    // Método auxiliar para deletar a imagem antiga
    private void deletarImagemAntiga(String nomeImagem) {
        if (nomeImagem == null || nomeImagem.isEmpty()) {
            return; // Nenhuma imagem para deletar
        }
        try {
            Path caminhoArquivo = Paths.get(uploadPath, nomeImagem);
            Files.deleteIfExists(caminhoArquivo);
        } catch (IOException e) {
            // Apenas loga o erro, não impede a operação principal de continuar
            System.err.println("Falha ao deletar imagem antiga: " + nomeImagem);
            e.printStackTrace();
        }
    }
}
