import { ICompra } from 'app/entities/compra/compra.model';

export interface ILoja {
  id?: number;
  nome?: string;
  endereco?: string | null;
  telefone?: string;
  compra?: ICompra | null;
}

export class Loja implements ILoja {
  constructor(
    public id?: number,
    public nome?: string,
    public endereco?: string | null,
    public telefone?: string,
    public compra?: ICompra | null
  ) {}
}

export function getLojaIdentifier(loja: ILoja): number | undefined {
  return loja.id;
}
