export type ApiError = { timestamp?: string; status: number; error: string; message: string; path?: string; };
const API_BASE = (import.meta as any).env?.VITE_API_BASE ?? "http://localhost:8080";
export async function apiPost<T>(path: string, body: any, token?: string): Promise<T> {
  const res = await fetch(`${API_BASE}${path}`, { method:"POST", headers:{ "Content-Type":"application/json", ...(token?{Authorization:`Bearer ${token}`}:{}) }, body: JSON.stringify(body) });
  const data = await res.json().catch(() => ({}));
  if (!res.ok) throw data as ApiError;
  return data as T;
}
export async function apiGet<T>(path: string, token?: string): Promise<T> {
  const res = await fetch(`${API_BASE}${path}`, { headers: token ? { Authorization: `Bearer ${token}` } : {} });
  const data = await res.json().catch(() => ([]));
  if (!res.ok) throw data as ApiError;
  return data as T;
}
