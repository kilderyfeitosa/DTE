package br.com.appbase.view.viewmodel;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import java.util.List;

import br.com.appbase.dominio.model.Local;
import br.com.appbase.dominio.model.TipoMaterial;
import br.com.appbase.modelo.repository.LocalRepository;
import br.com.appbase.modelo.repository.TiposMateriaisRepository;

public class CadastroLocalViewModel extends ViewModel {

    private LocalRepository localRepository = new LocalRepository();
    private TiposMateriaisRepository tiposMateriaisRepository = new TiposMateriaisRepository();

    public MutableLiveData<Boolean> carregando = new MutableLiveData<>();
    public MutableLiveData<List<TipoMaterial>> tiposMateriais = new MutableLiveData<>();
    public MutableLiveData<Boolean> localSalvo = new MutableLiveData<>();


    public void buscarTiposMateriais() {
        carregando.setValue(true);
        tiposMateriaisRepository.buscarTodos(new TiposMateriaisRepository.FirebaseDatabaseListCallback<TipoMaterial>() {
            @Override
            public void onSuccess(List<TipoMaterial> result) {
                tiposMateriais.setValue(result);
                carregando.setValue(false);
            }

            @Override
            public void onError(Exception e) {
                tiposMateriais.setValue(null);
                carregando.setValue(false);
            }
        });
    }

    public void salvarLocal(final Local local) {
        localRepository.salvarLocal(local, new LocalRepository.FirebaseDatabasePersistenceCallback() {
            @Override
            public void onSuccess(Boolean result) {
                localSalvo.setValue(true);
            }

            @Override
            public void onError(Exception e) {
                localSalvo.setValue(false);
            }
        });
    }

    @Override
    protected void onCleared() {
        localRepository.removeListener();
    }
}
