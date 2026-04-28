import { useEffect, useState } from 'react'

interface Rapport {
  idRapport: string
  dateRapport: string
  consommationTotale: number
  batterieDebut: number
  batterieFin: number
}

export default function Rapports() {
  const [rapports, setRapports] = useState<Rapport[]>([])
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState('')

  useEffect(() => {
    fetch('http://localhost:8080/ElectriMadaProject/api/rapports')
      .then(res => res.json())
      .then(data => {
        if (data.success) {
          setRapports(data.data || [])
        } else {
          setError(data.message || 'Erreur de chargement')
        }
        setLoading(false)
      })
      .catch(err => {
        setError('Erreur: ' + err.message)
        setLoading(false)
      })
  }, [])

  if (loading) return <div className="loading">Chargement...</div>
  if (error) return <div className="error">{error}</div>

  return (
    <div>
      <h1>Rapports Journaliers</h1>
      <p>Historique de consommation et production du village</p>

      {rapports.length === 0 ? (
        <p>Aucun rapport disponible</p>
      ) : (
        <table className="table">
          <thead>
            <tr>
              <th>Date</th>
              <th>Consommation (kWh)</th>
              <th>Batterie Début (%)</th>
              <th>Batterie Fin (%)</th>
              <th>Variation</th>
            </tr>
          </thead>
          <tbody>
            {rapports.map(r => {
              const variation = r.batterieFin - r.batterieDebut
              return (
                <tr key={r.idRapport}>
                  <td>{new Date(r.dateRapport).toLocaleDateString('fr-FR')}</td>
                  <td>{r.consommationTotale.toFixed(1)}</td>
                  <td>{r.batterieDebut.toFixed(1)}%</td>
                  <td>{r.batterieFin.toFixed(1)}%</td>
                  <td style={{ color: variation >= 0 ? 'green' : 'red' }}>
                    {variation >= 0 ? '+' : ''}{variation.toFixed(1)}%
                  </td>
                </tr>
              )
            })}
          </tbody>
        </table>
      )}
    </div>
  )
}
