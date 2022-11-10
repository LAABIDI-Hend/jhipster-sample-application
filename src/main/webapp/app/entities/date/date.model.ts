export interface IDate {
  id: number;
  day?: string | null;
  month?: string | null;
  year?: string | null;
}

export type NewDate = Omit<IDate, 'id'> & { id: null };
