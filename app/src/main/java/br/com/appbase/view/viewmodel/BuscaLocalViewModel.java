package br.com.appbase.view.viewmodel;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

import br.com.appbase.dominio.model.Local;
import br.com.appbase.dominio.model.TipoMaterial;
import br.com.appbase.modelo.repository.LocalRepository;

public class BuscaLocalViewModel extends ViewModel {

    private LocalRepository localRepository = new LocalRepository();
    private LocalRepository meusLocaisRepository = new LocalRepository();
    public MutableLiveData<List<Local>> locais = new MutableLiveData<>();
    public MutableLiveData<List<Local>> meusLocais = new MutableLiveData<>();


    public void buscarTodosLocais() {
        buscarLocais();
    }

    public void buscarTodosMeusLocais() {
        buscarMeusLocais();
    }

    private void buscarLocais() {
        localRepository.buscarLocais(new LocalRepository.FirebaseDatabaseListCallback<Local>() {
            @Override
            public void onSuccess(List<Local> result) {
                locais.setValue(result);
            }

            @Override
            public void onError(Exception e) {
                locais.setValue(null);
            }
        });
    }

    private void buscarMeusLocais() {
        meusLocaisRepository.buscarMeusLocais(new LocalRepository.FirebaseDatabaseListCallback<Local>() {
            @Override
            public void onSuccess(List<Local> result) {
                meusLocais.setValue(result);
            }

            @Override
            public void onError(Exception e) {
                meusLocais.setValue(null);
            }
        });
    }

    public List<Local> buscarPorNomeOuMaterial(List<Local> todosLocais, String textoInformado) {
        List<Local> locaisFiltrados = new ArrayList<>();
        for (Local local : todosLocais) {
            //Filtrar por Nome
            if (local.getNome().toLowerCase().contains(textoInformado.trim().toLowerCase())) {
                locaisFiltrados.add(local);
            }

            //Filtrar por Nome Material
            for (TipoMaterial materialAceito : local.getMateriaisAceitos()) {
                if (materialAceito.getDescricao().toLowerCase().contains(textoInformado.trim().toLowerCase())) {
                    locaisFiltrados.add(local);
                }
            }
        }

        return locaisFiltrados;
    }

    @Override
    protected void onCleared() {
        localRepository.removeListener();
    }
}
