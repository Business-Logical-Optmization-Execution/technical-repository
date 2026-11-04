import { useEffect } from 'react';

export function useTituloDocumento(titulo) {
    useEffect(() => {
        document.title = titulo + ' · Portal Óttimos';
    }, [titulo]);
}
