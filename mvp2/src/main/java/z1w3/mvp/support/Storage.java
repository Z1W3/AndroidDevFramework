package z1w3.mvp.support;

import java.util.HashMap;

enum Storage {
    INSTANCE;

    private final int ATTACH_FINISHED = 1;
    private final int CREATE_FINISHED = 2;

    private final HashMap<Class<?>, BasePresenter> mSingletonMap = new HashMap<>();
    private final HashMap<Class<?>, Integer> mMarkMap = new HashMap<>();


    protected void addSingleton(BasePresenter presenter){
        if (!mSingletonMap.containsKey(presenter.getClass())) {
            mSingletonMap.put(presenter.getClass(), presenter);
        }
    }

    protected BasePresenter getSingleton(BasePresenter presenter){
        return mSingletonMap.get(presenter.getClass());
    }

    protected Boolean isSingleton(BasePresenter presenter){
        return mSingletonMap.containsKey(presenter.getClass());
    }

    protected void modifyAttachMark(BasePresenter presenter){
        mMarkMap.put(presenter.getClass(), ATTACH_FINISHED);
    }

    protected void modifyCreatedMark(BasePresenter presenter){
        mMarkMap.put(presenter.getClass(), CREATE_FINISHED);
    }

    protected Boolean isAttachFinishedMark(BasePresenter presenter){
        final Integer result = mMarkMap.get(presenter.getClass());
        return result != null && (result == ATTACH_FINISHED || result == CREATE_FINISHED);
    }

    protected Boolean isCreatFinishedMark(BasePresenter presenter){
        final Integer result = mMarkMap.get(presenter.getClass());
        return result != null && result == CREATE_FINISHED;
    }
}
